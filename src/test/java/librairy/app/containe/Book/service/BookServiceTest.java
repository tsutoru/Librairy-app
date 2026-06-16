package librairy.app.containe.Book.service;

import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;
    private Book book;

    @BeforeEach
    void setUp() {
        bookService = new BookService();

        book = new Book();
        book.setId("book-1");
        book.setTitle("Clean Code");
        book.setDescription("A book about writing clean code");
        book.setPrice(29.99);
        book.setIsbn("978-0132350884");

        bookService.Create(book);
    }

    @Test
    void create_shouldAddBookToList() {
        Book newBook = new Book();
        newBook.setId("book-2");
        newBook.setTitle("Effective Java");

        Book result = bookService.Create(newBook);

        assertEquals(2, bookService.getAllBooks().size());
        assertEquals("Effective Java", result.getTitle());
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void getBookById_shouldReturnBook_whenExists() {
        Book result = bookService.getBookById("book-1");

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
    }

    @Test
    void getBookById_shouldReturnNull_whenNotFound() {
        Book result = bookService.getBookById("unknown-id");

        assertNull(result);
    }

    @Test
    void search_shouldFilterByTitle() {
        List<Book> result = bookService.search("clean", null, null, null);

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void search_shouldFilterByIsbn() {
        List<Book> result = bookService.search(null, null, null, "978-0132350884");

        assertEquals(1, result.size());
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatch() {
        List<Book> result = bookService.search("nonexistent title", null, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_shouldModifyExistingBook() {
        Book updatedData = new Book();
        updatedData.setTitle("Clean Code - 2nd Edition");
        updatedData.setDescription("Updated description");
        updatedData.setPrice(34.99);
        updatedData.setIsbn("978-0132350884");

        Book result = bookService.update("book-1", updatedData);

        assertNotNull(result);
        assertEquals("Clean Code - 2nd Edition", result.getTitle());
        assertEquals(34.99, result.getPrice());
    }

    @Test
    void update_shouldReturnNull_whenBookNotFound() {
        Book updatedData = new Book();
        updatedData.setTitle("Doesn't matter");

        Book result = bookService.update("unknown-id", updatedData);

        assertNull(result);
    }

    @Test
    void delete_shouldRemoveBookFromList() {
        bookService.delete("book-1");

        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    void delete_shouldDoNothing_whenIdNotFound() {
        bookService.delete("unknown-id");

        assertEquals(1, bookService.getAllBooks().size());
    }
}