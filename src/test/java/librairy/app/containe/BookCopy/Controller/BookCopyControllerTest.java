package librairy.app.containe.BookCopy.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

import librairy.app.containe.bookCopy.controller.BookCopyController;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.service.BookCopyService;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookCopyController.class)
class BookCopyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookCopyService bookCopyService;

    private BookCopy bookCopy;

    @BeforeEach
    void setUp() {
        bookCopy = new BookCopy();
        bookCopy.setId("copy-550e8400-e29b-41d4-a716-446655440000");
        bookCopy.setStatus(CopyStatus.AVAILABLE);
    }

    @Test
    void create_shouldReturn201_whenValidCopy() throws Exception {
        when(bookCopyService.create(any(BookCopy.class))).thenReturn(bookCopy);

        mockMvc
                .perform(
                        post("/book-copies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookCopy)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void getAll_shouldReturn200_withListOfCopies() throws Exception {
        when(bookCopyService.getAll()).thenReturn(List.of(bookCopy));

        mockMvc
                .perform(get("/book-copies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void getById_shouldReturn200_whenCopyExists() throws Exception {
        when(bookCopyService.getById("copy-550e8400-e29b-41d4-a716-446655440000"))
                .thenReturn(bookCopy);

        mockMvc
                .perform(get("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void getById_shouldReturn404_whenCopyNotFound() throws Exception {
        when(bookCopyService.getById("copy-550e8400-e29b-41d4-a716-446655440000"))
                .thenThrow(new NotFoundException("BookCopy not found"));

        mockMvc
                .perform(get("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailable_shouldReturn200_withAvailableCopies() throws Exception {
        when(bookCopyService.getAvailable(null)).thenReturn(List.of(bookCopy));

        mockMvc
                .perform(get("/book-copies/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void getAvailable_shouldFilterByBookId() throws Exception {
        when(bookCopyService.getAvailable("book-1")).thenReturn(List.of(bookCopy));

        mockMvc
                .perform(get("/book-copies/available").param("bookId", "book-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    void getStockByBookId_shouldReturn200_withStockCount() throws Exception {
        when(bookCopyService.getStockByBookId("book-550e8400-e29b-41d4-a716-446655440000"))
                .thenReturn(5);

        mockMvc
                .perform(get("/book-copies/book-550e8400-e29b-41d4-a716-446655440000/copies/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    @Test
    void getStockByBookId_shouldReturn400_whenInvalidUUID() throws Exception {
        when(bookCopyService.getStockByBookId("invalid-uuid"))
                .thenThrow(new BadRequestException("Invalid UUID format"));

        mockMvc
                .perform(get("/book-copies/invalid-uuid/copies/stock"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_shouldReturn200_whenValidStatus() throws Exception {
        bookCopy.setStatus(CopyStatus.SOLD);
        when(bookCopyService.updateStatus(
                eq("copy-550e8400-e29b-41d4-a716-446655440000"), eq("SOLD")))
                .thenReturn(bookCopy);

        mockMvc
                .perform(
                        put("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Map.of("status", "SOLD"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SOLD"));
    }

    @Test
    void updateStatus_shouldReturn400_whenInvalidStatus() throws Exception {
        when(bookCopyService.updateStatus(
                eq("copy-550e8400-e29b-41d4-a716-446655440000"), eq("INVALID")))
                .thenThrow(new BadRequestException("Invalid status: INVALID"));

        mockMvc
                .perform(
                        put("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Map.of("status", "INVALID"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldReturn204_whenCopyExists() throws Exception {
        doNothing().when(bookCopyService)
                .delete("copy-550e8400-e29b-41d4-a716-446655440000");

        mockMvc
                .perform(delete("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404_whenCopyNotFound() throws Exception {
        doThrow(new NotFoundException("BookCopy not found"))
                .when(bookCopyService)
                .delete("copy-550e8400-e29b-41d4-a716-446655440000");

        mockMvc
                .perform(delete("/book-copies/copy-550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(status().isNotFound());
    }
}