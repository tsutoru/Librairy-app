package librairy.app.containe.book.service.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import librairy.app.containe.book.dto.BookExternalDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class GoogleBooksService {

  private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final String apiKey;

  public GoogleBooksService(
      RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${google.books.api.key:}") String apiKey) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
    this.apiKey = apiKey;
  }

  public BookExternalDTO getBookByIsbn(String isbn) {
    return items(isbn)
        .filter(items -> !items.isEmpty())
        .map(items -> items.get(0))
        .map(item -> mapToDto(item, isbn))
        .orElseGet(
            () -> {
              log.warn("Aucun livre trouvé pour l'ISBN: {}", isbn);
              return null;
            });
  }

  public List<BookExternalDTO> searchBooksByIsbn(String isbn) {
    return items(isbn)
        .map(
            items -> {
              List<BookExternalDTO> books = new ArrayList<>();
              items.forEach(
                  item -> {
                    BookExternalDTO book = mapToDto(item, isbn);
                    if (book != null) {
                      books.add(book);
                    }
                  });
              return books;
            })
        .orElseGet(List::of);
  }

  private Optional<JsonNode> items(String isbn) {
    return fetchJson(buildUrl(isbn))
        .map(root -> root.get("items"))
        .filter(node -> node != null && node.isArray());
  }

  private String buildUrl(String isbn) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(GOOGLE_BOOKS_API_URL).queryParam("q", "isbn:" + isbn);

    if (apiKey != null && !apiKey.isBlank()) {
      builder.queryParam("key", apiKey);
    }

    return builder.toUriString();
  }

  private Optional<JsonNode> fetchJson(String url) {
    try {
      String response = restTemplate.getForObject(url, String.class);
      if (response == null || response.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(objectMapper.readTree(response));
    } catch (RestClientException | java.io.IOException e) {
      log.error("Erreur lors de l'appel à Google Books: {}", url, e);
      return Optional.empty();
    }
  }

  private BookExternalDTO mapToDto(JsonNode item, String isbn) {
    JsonNode volumeInfo = field(item, "volumeInfo").orElse(null);
    if (volumeInfo == null) {
      return null;
    }

    return BookExternalDTO.builder()
        .id("GB-" + text(item, "id"))
        .isbn(isbn)
        .title(text(volumeInfo, "title"))
        .description(text(volumeInfo, "description"))
        .authorName(firstArrayText(volumeInfo, "authors"))
        .publicationDate(text(volumeInfo, "publishedDate"))
        .publisher(text(volumeInfo, "publisher"))
        .pageCount(intValue(volumeInfo, "pageCount"))
        .coverImageUrl(nestedText(volumeInfo, "imageLinks", "thumbnail"))
        .price(price(item))
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

  private String firstArrayText(JsonNode node, String arrayField) {
    return field(node, arrayField)
        .filter(JsonNode::isArray)
        .filter(arr -> !arr.isEmpty())
        .map(arr -> arr.get(0).asText())
        .orElse(null);
  }

  private String nestedText(JsonNode node, String parentField, String childField) {
    return field(node, parentField)
        .flatMap(parent -> field(parent, childField))
        .map(JsonNode::asText)
        .orElse(null);
  }

  private Double price(JsonNode item) {
    return field(item, "saleInfo")
        .flatMap(saleInfo -> field(saleInfo, "listPrice"))
        .flatMap(listPrice -> field(listPrice, "amount"))
        .map(JsonNode::asDouble)
        .orElse(null);
  }
}
