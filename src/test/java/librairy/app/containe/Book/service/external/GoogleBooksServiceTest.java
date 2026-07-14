package librairy.app.containe.Book.service.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.service.external.GoogleBooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class GoogleBooksServiceTest {

  @Mock private RestTemplate restTemplate;

  private GoogleBooksService googleBooksService;

  @BeforeEach
  void setUp() {
    googleBooksService = new GoogleBooksService(restTemplate, new ObjectMapper(), "test-key");
  }

  @Test
  void getBookByIsbn_returnsBook_whenItemFound() {
    String json =
        """
        {
          "items": [
            {
              "id": "abc123",
              "volumeInfo": {
                "title": "Harry Potter and the Philosopher's Stone",
                "authors": ["J. K. Rowling"],
                "publisher": "Bloomsbury",
                "publishedDate": "1997",
                "pageCount": 223,
                "imageLinks": { "thumbnail": "http://cover.jpg" }
              }
            }
          ]
        }
        """;
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

    BookExternalDTO result = googleBooksService.getBookByIsbn("9780439708180");

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("GB-abc123");
    assertThat(result.getTitle()).isEqualTo("Harry Potter and the Philosopher's Stone");
    assertThat(result.getAuthorName()).isEqualTo("J. K. Rowling");
    assertThat(result.getPublisher()).isEqualTo("Bloomsbury");
    assertThat(result.getPageCount()).isEqualTo(223);
    assertThat(result.getCoverImageUrl()).isEqualTo("http://cover.jpg");
  }

  @Test
  void getBookByIsbn_returnsNull_whenNoItems() {
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");

    BookExternalDTO result = googleBooksService.getBookByIsbn("0000000000000");

    assertThat(result).isNull();
  }

  @Test
  void getBookByIsbn_returnsNull_whenRestTemplateThrows() {
    when(restTemplate.getForObject(anyString(), eq(String.class)))
        .thenThrow(new RestClientException("timeout"));

    BookExternalDTO result = googleBooksService.getBookByIsbn("9780439708180");

    assertThat(result).isNull();
  }

  @Test
  void searchBooksByIsbn_returnsAllMappedBooks() {
    String json =
        """
        {
          "items": [
            { "id": "id1", "volumeInfo": { "title": "Book One" } },
            { "id": "id2", "volumeInfo": { "title": "Book Two" } }
          ]
        }
        """;
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

    List<BookExternalDTO> results = googleBooksService.searchBooksByIsbn("9780439708180");

    assertThat(results)
        .hasSize(2)
        .extracting(BookExternalDTO::getTitle)
        .containsExactly("Book One", "Book Two");
  }

  @Test
  void searchBooksByIsbn_returnsEmptyList_whenNoItemsField() {
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");

    List<BookExternalDTO> results = googleBooksService.searchBooksByIsbn("0000000000000");

    assertThat(results).isEmpty();
  }
}
