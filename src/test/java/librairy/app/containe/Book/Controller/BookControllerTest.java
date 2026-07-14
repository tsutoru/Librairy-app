package librairy.app.containe.Book.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import librairy.app.containe.book.controller.BookController;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private BookService bookService;

  private Book book;

  @BeforeEach
  void setUp() {
    book = new Book();
    book.setId("550e8400-e29b-41d4-a716-446655440000");
    book.setTitle("Clean Code");
    book.setDescription("A book about writing clean code");
    book.setIsbn("978-0132350884");
  }

  @Test
  void createBook_shouldReturn201_whenValidBook() throws Exception {
    when(bookService.create(any(Book.class))).thenReturn(book);

    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Clean Code"))
        .andExpect(jsonPath("$.isbn").value("978-0132350884"));
  }

  @Test
  void getAllBooks_shouldReturn200_withListOfBooks() throws Exception {
    when(bookService.getAllBooks()).thenReturn(List.of(book));

    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Clean Code"));
  }

  @Test
  void getBookById_shouldReturn200_whenBookExists() throws Exception {
    when(bookService.getBookById("550e8400-e29b-41d4-a716-446655440000")).thenReturn(book);

    mockMvc
        .perform(get("/books/550e8400-e29b-41d4-a716-446655440000"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Clean Code"));
  }

  @Test
  void getBookById_shouldReturn404_whenBookNotFound() throws Exception {
    when(bookService.getBookById("550e8400-e29b-41d4-a716-446655440000"))
        .thenThrow(new NotFoundException("Book not found"));

    mockMvc
        .perform(get("/books/550e8400-e29b-41d4-a716-446655440000"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getBookById_shouldReturn400_whenInvalidUUID() throws Exception {
    when(bookService.getBookById("invalid-uuid"))
        .thenThrow(new BadRequestException("Invalid UUID format"));

    mockMvc.perform(get("/books/invalid-uuid")).andExpect(status().isBadRequest());
  }

  @Test
  void searchBook_shouldReturn200_withResults() throws Exception {
    when(bookService.search("clean", null, null, null)).thenReturn(List.of(book));

    mockMvc
        .perform(get("/books/search").param("title", "clean"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Clean Code"));
  }

  @Test
  void searchBook_shouldReturnEmptyList_whenNoMatch() throws Exception {
    when(bookService.search("nonexistent", null, null, null)).thenReturn(List.of());

    mockMvc
        .perform(get("/books/search").param("title", "nonexistent"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void updateBook_shouldReturn200_whenBookExists() throws Exception {
    Book updated = new Book();
    updated.setId("550e8400-e29b-41d4-a716-446655440000");
    updated.setTitle("Clean Code 2nd Edition");

    when(bookService.update(eq("550e8400-e29b-41d4-a716-446655440000"), any(Book.class)))
        .thenReturn(updated);

    mockMvc
        .perform(
            put("/books/550e8400-e29b-41d4-a716-446655440000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Clean Code 2nd Edition"));
  }

  @Test
  void updateBook_shouldReturn404_whenBookNotFound() throws Exception {
    when(bookService.update(eq("550e8400-e29b-41d4-a716-446655440000"), any(Book.class)))
        .thenThrow(new NotFoundException("Book not found"));

    mockMvc
        .perform(
            put("/books/550e8400-e29b-41d4-a716-446655440000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteBook_shouldReturn204_whenBookExists() throws Exception {
    doNothing().when(bookService).delete("550e8400-e29b-41d4-a716-446655440000");

    mockMvc
        .perform(delete("/books/550e8400-e29b-41d4-a716-446655440000"))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteBook_shouldReturn404_whenBookNotFound() throws Exception {
    doThrow(new NotFoundException("Book not found"))
        .when(bookService)
        .delete("550e8400-e29b-41d4-a716-446655440000");

    mockMvc
        .perform(delete("/books/550e8400-e29b-41d4-a716-446655440000"))
        .andExpect(status().isNotFound());
  }
}
