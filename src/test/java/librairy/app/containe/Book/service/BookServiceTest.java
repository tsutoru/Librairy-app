package librairy.app.containe.Book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.book.service.BookService;
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

  @BeforeEach
  void setUp() {
    book = new Book();
    book.setId("book-1");
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
  void getAllBooks_shouldReturnAllBooks() {
    when(bookRepository.findAll()).thenReturn(List.of(book));

    List<Book> result = bookService.getAllBooks();

    assertEquals(1, result.size());
    assertEquals("Clean Code", result.get(0).getTitle());
    verify(bookRepository, times(1)).findAll();
  }

  @Test
  void getBookById_shouldReturnBook_whenExists() {
    when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

    Book result = bookService.getBookById("book-1");

    assertNotNull(result);
    assertEquals("Clean Code", result.getTitle());
    verify(bookRepository, times(1)).findById("book-1");
  }

  @Test
  void getBookById_shouldReturnNull_whenNotFound() {
    when(bookRepository.findById("unknown-id")).thenReturn(Optional.empty());

    Book result = bookService.getBookById("unknown-id");

    assertNull(result);
    verify(bookRepository, times(1)).findById("unknown-id");
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
    updatedBook.setId("book-1");
    updatedBook.setTitle("Clean Code - 2nd Edition");
    updatedBook.setDescription("Updated description");
    updatedBook.setPrice(34.99);
    updatedBook.setIsbn("978-0132350884");

    Book updatedData = new Book();
    updatedData.setTitle("Clean Code - 2nd Edition");
    updatedData.setDescription("Updated description");
    updatedData.setPrice(34.99);
    updatedData.setIsbn("978-0132350884");

    when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
    when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

    Book result = bookService.update("book-1", updatedData);

    assertNotNull(result);
    assertEquals("Clean Code - 2nd Edition", result.getTitle());
    assertEquals(34.99, result.getPrice());
    verify(bookRepository, times(1)).findById("book-1");
    verify(bookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void update_shouldReturnNull_whenBookNotFound() {
    Book updatedData = new Book();
    updatedData.setTitle("Doesn't matter");

    when(bookRepository.findById("unknown-id")).thenReturn(Optional.empty());

    Book result = bookService.update("unknown-id", updatedData);

    assertNull(result);
    verify(bookRepository, times(1)).findById("unknown-id");
    verify(bookRepository, never()).save(any(Book.class));
  }

  @Test
  void delete_shouldRemoveBookFromList() {
    doNothing().when(bookRepository).deleteById("book-1");

    bookService.delete("book-1");

    verify(bookRepository, times(1)).deleteById("book-1");
  }

  @Test
  void delete_shouldDoNothing_whenIdNotFound() {
    doNothing().when(bookRepository).deleteById("unknown-id");

    bookService.delete("unknown-id");

    verify(bookRepository, times(1)).deleteById("unknown-id");
  }
}
