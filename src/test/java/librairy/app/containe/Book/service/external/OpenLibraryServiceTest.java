package librairy.app.containe.Book.service.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.service.external.OpenLibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class OpenLibraryServiceTest {

  @Mock private RestTemplate restTemplate;

  private OpenLibraryService openLibraryService;

  @BeforeEach
  void setUp() {
    openLibraryService = new OpenLibraryService(restTemplate, new ObjectMapper());
  }

  @Test
  void getBookByIsbn_returnsBook_whenIsbnKeyFound() {
    String json =
        """
        {
          "ISBN:9780439708180": {
            "title": "Harry Potter and the Philosopher's Stone",
            "authors": [{ "name": "J. K. Rowling" }],
            "publish_date": "1997",
            "publishers": [{ "name": "Bloomsbury" }],
            "number_of_pages": 223,
            "cover": { "id": 240726 }
          }
        }
        """;
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

    BookExternalDTO result = openLibraryService.getBookByIsbn("9780439708180");

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo("OL-9780439708180");
    assertThat(result.getTitle()).isEqualTo("Harry Potter and the Philosopher's Stone");
    assertThat(result.getAuthorName()).isEqualTo("J. K. Rowling");
    assertThat(result.getPublisher()).isEqualTo("Bloomsbury");
    assertThat(result.getPageCount()).isEqualTo(223);
    assertThat(result.getCoverImageUrl())
        .isEqualTo("https://covers.openlibrary.org/b/id/240726-M.jpg");
  }

  @Test
  void getBookByIsbn_returnsNull_whenKeyMissing() {
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");

    BookExternalDTO result = openLibraryService.getBookByIsbn("0000000000000");

    assertThat(result).isNull();
  }

  @Test
  void getBookByIsbn_returnsNull_whenResponseEmpty() {
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("");

    BookExternalDTO result = openLibraryService.getBookByIsbn("9780439708180");

    assertThat(result).isNull();
  }

  @Test
  void searchBooksByIsbn_returnsAllMappedBooks() {
    String json =
        """
        {
          "docs": [
            {
              "key": "/works/OL1W",
              "title": "Book One",
              "author_name": ["Author A"],
              "first_publish_year": 2001,
              "publisher": ["Publisher A"],
              "cover_i": 111
            },
            {
              "key": "/works/OL2W",
              "title": "Book Two"
            }
          ]
        }
        """;
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

    List<BookExternalDTO> results = openLibraryService.searchBooksByIsbn("9780439708180");

    assertThat(results).hasSize(2);
    assertThat(results.get(0).getId()).isEqualTo("OL-/works/OL1W");
    assertThat(results.get(0).getAuthorName()).isEqualTo("Author A");
    assertThat(results.get(0).getPublicationDate()).isEqualTo("2001");
    assertThat(results.get(0).getCoverImageUrl())
        .isEqualTo("https://covers.openlibrary.org/b/id/111-M.jpg");
  }

  @Test
  void searchBooksByIsbn_returnsEmptyList_whenDocsMissing() {
    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");

    List<BookExternalDTO> results = openLibraryService.searchBooksByIsbn("0000000000000");

    assertThat(results).isEmpty();
  }
}
