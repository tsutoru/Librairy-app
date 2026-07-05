package librairy.app.containe.Author.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import librairy.app.containe.Author.entity.Author;
import librairy.app.containe.Author.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthorService authorService;

  @Autowired private ObjectMapper objectMapper;

  private Author author;

  @BeforeEach
  void setUp() {
    author = new Author();
    author.setId("1");
    author.setFirstName("George");
    author.setLastName("Orwell");
    author.setBiography("English novelist");
    author.setNationality("British");
  }

  @Test
  void create_shouldReturnCreatedAuthor() throws Exception {
    when(authorService.create(any(Author.class))).thenReturn(author);

    mockMvc
        .perform(
            post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.firstName").value("George"))
        .andExpect(jsonPath("$.lastName").value("Orwell"));

    verify(authorService, times(1)).create(any(Author.class));
  }

  @Test
  void getAll_shouldReturnListOfAuthors() throws Exception {
    when(authorService.getAll()).thenReturn(List.of(author));

    mockMvc
        .perform(get("/authors"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value("1"));

    verify(authorService, times(1)).getAll();
  }

  @Test
  void getById_shouldReturnAuthor() throws Exception {
    when(authorService.getById("1")).thenReturn(author);

    mockMvc
        .perform(get("/authors/{id}", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.lastName").value("Orwell"));

    verify(authorService, times(1)).getById("1");
  }

  @Test
  void getByLastName_shouldReturnMatchingAuthors() throws Exception {
    when(authorService.getByLastName("Orwell")).thenReturn(List.of(author));

    mockMvc
        .perform(get("/authors/search").param("lastName", "Orwell"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].lastName").value("Orwell"));

    verify(authorService, times(1)).getByLastName("Orwell");
  }

  @Test
  void update_shouldReturnUpdatedAuthor() throws Exception {
    Author updated = new Author();
    updated.setId("1");
    updated.setFirstName("George");
    updated.setLastName("Orwell");
    updated.setBiography("Updated biography");
    updated.setNationality("British");

    when(authorService.update(eq("1"), any(Author.class))).thenReturn(updated);

    mockMvc
        .perform(
            put("/authors/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.biography").value("Updated biography"));

    verify(authorService, times(1)).update(eq("1"), any(Author.class));
  }

  @Test
  void delete_shouldReturnNoContent() throws Exception {
    doNothing().when(authorService).delete("1");

    mockMvc.perform(delete("/authors/{id}", "1")).andExpect(status().isNoContent());

    verify(authorService, times(1)).delete("1");
  }
}
