package librairy.app.containe.Book.Controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import librairy.app.containe.book.controller.ExternalBookController;
import librairy.app.containe.book.dto.BookExternalDTO;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import librairy.app.containe.book.service.ExternalBookIntegrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExternalBookController.class)
class ExternalBookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ExternalBookIntegrationService externalBookIntegrationService;

  @MockBean private BookService bookService;

  @Test
  void getBookByIsbn_returns200_whenBooksFound() throws Exception {
    BookExternalDTO dto =
        BookExternalDTO.builder()
            .id("OL-123")
            .title("Harry Potter and the Philosopher's Stone")
            .isbn("9780439708180")
            .build();

    when(externalBookIntegrationService.getBookFromExternalSources("9780439708180"))
        .thenReturn(List.of(dto));

    mockMvc
        .perform(get("/books/external/isbn/9780439708180"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Harry Potter and the Philosopher's Stone"));
  }

  @Test
  void getBookByIsbn_returns404_whenNoBooksFound() throws Exception {
    when(externalBookIntegrationService.getBookFromExternalSources("0000000000000"))
        .thenReturn(List.of());

    mockMvc.perform(get("/books/external/isbn/0000000000000")).andExpect(status().isNotFound());
  }

  @Test
  void searchBooks_returns200_withResults() throws Exception {
    BookExternalDTO dto =
        BookExternalDTO.builder().id("GB-abc").title("Book One").isbn("9780439708180").build();

    when(externalBookIntegrationService.searchBooksByIsbn(anyString())).thenReturn(List.of(dto));

    mockMvc
        .perform(get("/books/external/search").param("isbn", "9780439708180"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("GB-abc"));
  }

  @Test
  void searchBooks_returns200_withEmptyList() throws Exception {
    when(externalBookIntegrationService.searchBooksByIsbn(anyString())).thenReturn(List.of());

    mockMvc
        .perform(get("/books/external/search").param("isbn", "0000000000000"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void importBook_returns200_whenBookFoundAndCreated() throws Exception {
    BookExternalDTO externalBook =
        BookExternalDTO.builder()
            .title("Harry Potter and the Philosopher's Stone")
            .isbn("9780439708180")
            .build();
    Book savedBook =
        Book.builder()
            .id("uuid-1")
            .title("Harry Potter and the Philosopher's Stone")
            .isbn("9780439708180")
            .build();

    when(externalBookIntegrationService.getBookFromExternalSources("9780439708180"))
        .thenReturn(List.of(externalBook));
    when(bookService.create(org.mockito.ArgumentMatchers.any(Book.class))).thenReturn(savedBook);

    mockMvc
        .perform(post("/books/external/import/9780439708180"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("uuid-1"));
  }

  @Test
  void importBook_returns404_whenBookNotFound() throws Exception {
    when(externalBookIntegrationService.getBookFromExternalSources("0000000000000"))
        .thenReturn(List.of());

    mockMvc.perform(post("/books/external/import/0000000000000")).andExpect(status().isNotFound());
  }
}
