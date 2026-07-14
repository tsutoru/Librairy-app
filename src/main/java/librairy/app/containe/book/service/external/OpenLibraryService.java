package librairy.app.containe.book.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import librairy.app.containe.book.dto.BookExternalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OpenLibraryService {

  private static final String OPENLIBRARY_API_URL =
      "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";
  private static final String OPENLIBRARY_SEARCH_URL =
      "https://openlibrary.org/search.json?isbn=%s";
  private static final String COVER_URL = "https://covers.openlibrary.org/b/id/%s-M.jpg";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public OpenLibraryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  public BookExternalDTO getBookByIsbn(String isbn) {
    String key = "ISBN:" + isbn;

    return fetchJson(String.format(OPENLIBRARY_API_URL, isbn))
        .map(root -> root.get(key))
        .filter(node -> !node.isMissingNode() && !node.isNull())
        .map(bookData -> mapToDto(bookData, isbn))
        .orElseGet(
            () -> {
              log.warn("ISBN {} non trouvé dans OpenLibrary", isbn);
              return null;
            });
  }

  public List<BookExternalDTO> searchBooksByIsbn(String isbn) {
    return fetchJson(String.format(OPENLIBRARY_SEARCH_URL, isbn))
        .map(root -> root.path("docs"))
        .filter(JsonNode::isArray)
        .map(
            docs -> {
              List<BookExternalDTO> books = new ArrayList<>();
              docs.forEach(doc -> books.add(mapSearchDocToDto(doc, isbn)));
              return books;
            })
        .orElseGet(List::of);
  }

  private Optional<JsonNode> fetchJson(String url) {
    try {
      String response = restTemplate.getForObject(url, String.class);
      if (response == null || response.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(objectMapper.readTree(response));
    } catch (RestClientException | java.io.IOException e) {
      log.error("Erreur lors de l'appel à OpenLibrary: {}", url, e);
      return Optional.empty();
    }
  }

  private BookExternalDTO mapToDto(JsonNode data, String isbn) {
    return BookExternalDTO.builder()
        .id("OL-" + isbn)
        .isbn(isbn)
        .title(text(data, "title"))
        .description(description(data))
        .authorName(firstArrayText(data, "authors", "name"))
        .publicationDate(text(data, "publish_date"))
        .publisher(firstArrayText(data, "publishers", "name"))
        .pageCount(intValue(data, "number_of_pages"))
        .coverImageUrl(coverUrl(data.path("cover"), "id"))
        .build();
  }

  private BookExternalDTO mapSearchDocToDto(JsonNode doc, String isbn) {
    return BookExternalDTO.builder()
        .id("OL-" + text(doc, "key"))
        .isbn(isbn)
        .title(text(doc, "title"))
        .authorName(firstArrayText(doc, "author_name"))
        .publisher(firstArrayText(doc, "publisher"))
        .publicationDate(intText(doc, "first_publish_year"))
        .coverImageUrl(coverUrl(doc, "cover_i"))
        .build();
  }

  private Optional<JsonNode> field(JsonNode node, String name) {
    return Optional.ofNullable(node).map(n -> n.get(name)).filter(n -> !n.isNull());
  }

  private String text(JsonNode node, String field) {
    return field(node, field).map(JsonNode::asText).orElse(null);
  }

  private Integer intValue(JsonNode node, String field) {
    return field(node, field).map(JsonNode::asInt).orElse(null);
  }

  private String intText(JsonNode node, String field) {
    return field(node, field).map(n -> String.valueOf(n.asInt())).orElse(null);
  }

  private String firstArrayText(JsonNode node, String arrayField, String subField) {
    return field(node, arrayField)
        .filter(JsonNode::isArray)
        .filter(arr -> !arr.isEmpty())
        .map(arr -> arr.get(0))
        .map(first -> text(first, subField))
        .orElse(null);
  }

  private String firstArrayText(JsonNode node, String arrayField) {
    return field(node, arrayField)
        .filter(JsonNode::isArray)
        .filter(arr -> !arr.isEmpty())
        .map(arr -> arr.get(0).asText())
        .orElse(null);
  }

  private String coverUrl(JsonNode node, String idField) {
    return field(node, idField).map(id -> String.format(COVER_URL, id.asText())).orElse(null);
  }

  private String description(JsonNode data) {
    return field(data, "description")
        .map(desc -> desc.isTextual() ? desc.asText() : text(desc, "value"))
        .orElse(null);
  }
}
