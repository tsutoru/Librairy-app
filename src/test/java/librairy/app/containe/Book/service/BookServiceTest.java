package librairy.app.containe.Book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.book.service.BookService;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepository bookRepository;

  @InjectMocks private BookService bookService;

  private Book book;
  private String validId;

  @BeforeEach
  void setUp() {
    validId = UUID.randomUUID().toString();
    book = new Book();
    book.setId(validId);
    book.setTitle("Clean Code");
    book.setDescription("A book about writing clean code");
    book.setPrice(29.99);
    book.setIsbn("978-0132350884");
  }

  @Test
  void create_shouldAddBookToList() {
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    Book result = bookService.create(book);

    assertNotNull(result);
    assertEquals("Clean Code", result.getTitle());
    verify(bookRepository, times(1)).save(book);
  }

  @Test
  void create_shouldGenerateId_whenIdIsNull() {
    Book newBook = new Book();
    newBook.setTitle("New Book");
    newBook.setDescription("Description");
    newBook.setPrice(19.99);

    when(bookRepository.save(any(Book.class)))
        .thenAnswer(
            invocation -> {
              Book saved = invocation.getArgument(0);
              saved.setId(UUID.randomUUID().toString());
              return saved;
            });

    Book result = bookService.create(newBook);

    assertNotNull(result.getId());
    assertEquals("New Book", result.getTitle());
    verify(bookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void getAllBooks_shouldReturnAllBooks() {
    when(bookRepository.findAll()).thenReturn(List.of(book));

    List<Book> result = bookService.getAllBooks();

    assertEquals(1, result.size());
    assertEquals("Clean Code", result.get(0).getTitle());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  void getBookById_shouldReturnBook_whenExists() {
    when(bookRepository.findById(validId)).thenReturn(Optional.of(book));

    Book result = bookService.getBookById(validId);

    assertNotNull(result);
    assertEquals("Clean Code", result.getTitle());
    verify(bookRepository, times(1)).findById(validId);
  }

  @Test
  void getBookById_shouldThrowNotFoundException_whenNotFound() {
    String nonExistentId = UUID.randomUUID().toString();
    when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookService.getBookById(nonExistentId));
    verify(bookRepository, times(1)).findById(nonExistentId);
  }

  @Test
  void getBookById_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(BadRequestException.class, () -> bookService.getBookById("invalid-id"));
    verify(bookRepository, never()).findById(any());
  }

  @Test
  void search_shouldFilterByTitle() {
    when(bookRepository.findAll()).thenReturn(List.of(book));

    List<Book> result = bookService.search("clean", null, null, null);

    assertEquals(1, result.size());
    assertEquals("Clean Code", result.get(0).getTitle());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  void search_shouldFilterByIsbn() {
    when(bookRepository.findAll()).thenReturn(List.of(book));

    List<Book> result = bookService.search(null, null, null, "978-0132350884");

    assertEquals(1, result.size());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  void search_shouldReturnEmptyList_whenNoMatch() {
    when(bookRepository.findAll()).thenReturn(List.of(book));

    List<Book> result = bookService.search("nonexistent title", null, null, null);

    assertTrue(result.isEmpty());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  void update_shouldModifyExistingBook() {
    Book updatedBook = new Book();
    updatedBook.setId(validId);
    updatedBook.setTitle("Clean Code - 2nd Edition");
    updatedBook.setDescription("Updated description");
    updatedBook.setPrice(34.99);
    updatedBook.setIsbn("978-0132350884");

    Book updatedData = new Book();
    updatedData.setTitle("Clean Code - 2nd Edition");
    updatedData.setDescription("Updated description");
    updatedData.setPrice(34.99);
    updatedData.setIsbn("978-0132350884");

    when(bookRepository.findById(validId)).thenReturn(Optional.of(book));
    when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

    Book result = bookService.update(validId, updatedData);

    assertNotNull(result);
    assertEquals("Clean Code - 2nd Edition", result.getTitle());
    assertEquals(34.99, result.getPrice());
    verify(bookRepository, times(1)).findById(validId);
    verify(bookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void update_shouldThrowNotFoundException_whenBookNotFound() {
    String nonExistentId = UUID.randomUUID().toString();
    Book updatedData = new Book();
    updatedData.setTitle("Doesn't matter");

    when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookService.update(nonExistentId, updatedData));
    verify(bookRepository, times(1)).findById(nonExistentId);
    verify(bookRepository, never()).save(any(Book.class));
  }

  @Test
  void update_shouldThrowBadRequestException_whenInvalidUUID() {
    Book updatedData = new Book();
    updatedData.setTitle("Doesn't matter");

    assertThrows(BadRequestException.class, () -> bookService.update("invalid-id", updatedData));
    verify(bookRepository, never()).findById(any());
    verify(bookRepository, never()).save(any(Book.class));
  }

  @Test
  void delete_shouldRemoveBookFromList() {
    when(bookRepository.findById(validId)).thenReturn(Optional.of(book));
    doNothing().when(bookRepository).deleteById(validId);

    bookService.delete(validId);

    verify(bookRepository, times(1)).findById(validId);
    verify(bookRepository, times(1)).deleteById(validId);
  }

  @Test
  void delete_shouldThrowNotFoundException_whenIdNotFound() {
    String nonExistentId = UUID.randomUUID().toString();
    when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookService.delete(nonExistentId));
    verify(bookRepository, times(1)).findById(nonExistentId);
    verify(bookRepository, never()).deleteById(any());
  }

  @Test
  void delete_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(BadRequestException.class, () -> bookService.delete("invalid-id"));
    verify(bookRepository, never()).findById(any());
    verify(bookRepository, never()).deleteById(any());
  }
}
