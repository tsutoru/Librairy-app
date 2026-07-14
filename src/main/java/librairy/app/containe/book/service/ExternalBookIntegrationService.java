package librairy.app.containe.book.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.service.external.GoogleBooksService;
import librairy.app.containe.book.service.external.OpenLibraryService;
import librairy.app.containe.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalBookIntegrationService {

  private static final Pattern ISBN_PATTERN = Pattern.compile("^(97[89])?\\d{9}(\\d|X)$");

  private final OpenLibraryService openLibraryService;
  private final GoogleBooksService googleBooksService;

  public ExternalBookIntegrationService(
      OpenLibraryService openLibraryService, GoogleBooksService googleBooksService) {
    this.openLibraryService = openLibraryService;
    this.googleBooksService = googleBooksService;
  }

  public List<BookExternalDTO> getBookFromExternalSources(String isbn) {
    String cleanIsbn = validateAndClean(isbn);

    List<BookExternalDTO> results =
        Stream.of(
                safeCall("OpenLibrary", cleanIsbn, openLibraryService::getBookByIsbn),
                safeCall("Google Books", cleanIsbn, googleBooksService::getBookByIsbn))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

    if (results.isEmpty()) {
      log.warn("Aucun livre trouvé pour l'ISBN: {}", cleanIsbn);
    }

    return results;
  }

  public List<BookExternalDTO> searchBooksByIsbn(String isbn) {
    String cleanIsbn = validateAndClean(isbn);

    return Stream.of(
            safeCallList("OpenLibrary", cleanIsbn, openLibraryService::searchBooksByIsbn),
            safeCallList("Google Books", cleanIsbn, googleBooksService::searchBooksByIsbn))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private String validateAndClean(String isbn) {
    String cleaned = isbn == null ? "" : isbn.replaceAll("[\\s-]", "").toUpperCase();

    if (!ISBN_PATTERN.matcher(cleaned).matches()) {
      throw new BadRequestException(
          "Format ISBN invalide: " + isbn + ". Attendu: ISBN-10 ou ISBN-13.");
    }

    return cleaned;
  }

  private Optional<BookExternalDTO> safeCall(
      String source, String isbn, Function<String, BookExternalDTO> call) {
    try {
      return Optional.ofNullable(call.apply(isbn));
    } catch (Exception e) {
      log.error("Erreur {} pour ISBN {}: {}", source, isbn, e.getMessage());
      return Optional.empty();
    }
  }

  private List<BookExternalDTO> safeCallList(
      String source, String isbn, Function<String, List<BookExternalDTO>> call) {
    try {
      return call.apply(isbn);
    } catch (Exception e) {
      log.error("Erreur recherche {} pour ISBN {}: {}", source, isbn, e.getMessage());
      return List.of();
    }
  }
}
