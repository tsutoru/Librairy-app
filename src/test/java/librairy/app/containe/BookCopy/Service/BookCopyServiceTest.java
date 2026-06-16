package librairy.app.containe.BookCopy.Service;

import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.service.BookCopyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookCopyServiceTest {

    private BookCopyService bookCopyService;
    private BookCopy copy;
    private Book book;

    @BeforeEach
    void setUp() {
        bookCopyService = new BookCopyService();

        book = new Book();
        book.setId("book-1");
        book.setTitle("Clean Code");

        copy = new BookCopy();
        copy.setId("copy-1");
        copy.setBook(book);
        copy.setStatus(CopyStatus.AVAILABLE);

        bookCopyService.create(copy);
    }

    @Test
    void create_shouldAddCopyToList() {
        BookCopy newCopy = new BookCopy();
        newCopy.setId("copy-2");
        newCopy.setStatus(CopyStatus.AVAILABLE);

        BookCopy result = bookCopyService.create(newCopy);

        assertEquals(2, bookCopyService.getAll().size());
        assertEquals("copy-2", result.getId());
    }

    @Test
    void getAll_shouldReturnAllCopies() {
        List<BookCopy> result = bookCopyService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getById_shouldReturnCopy_whenExists() {
        BookCopy result = bookCopyService.getById("copy-1");

        assertNotNull(result);
        assertEquals(CopyStatus.AVAILABLE, result.getStatus());
    }

    @Test
    void getById_shouldReturnNull_whenNotFound() {
        BookCopy result = bookCopyService.getById("unknown-id");

        assertNull(result);
    }

    @Test
    void getAvailable_shouldReturnOnlyAvailableCopies() {
        BookCopy soldCopy = new BookCopy();
        soldCopy.setId("copy-2");
        soldCopy.setBook(book);
        soldCopy.setStatus(CopyStatus.SOLD);
        bookCopyService.create(soldCopy);

        List<BookCopy> result = bookCopyService.getAvailable(null);

        assertEquals(1, result.size());
        assertEquals("copy-1", result.get(0).getId());
    }

    @Test
    void getAvailable_shouldFilterByBookId() {
        Book otherBook = new Book();
        otherBook.setId("book-2");

        BookCopy otherCopy = new BookCopy();
        otherCopy.setId("copy-2");
        otherCopy.setBook(otherBook);
        otherCopy.setStatus(CopyStatus.AVAILABLE);
        bookCopyService.create(otherCopy);

        List<BookCopy> result = bookCopyService.getAvailable("book-1");

        assertEquals(1, result.size());
        assertEquals("copy-1", result.get(0).getId());
    }

    @Test
    void updateStatus_shouldChangeStatus_whenCopyExists() {
        BookCopy result = bookCopyService.updateStatus("copy-1", "SOLD");

        assertNotNull(result);
        assertEquals(CopyStatus.SOLD, result.getStatus());
    }

    @Test
    void updateStatus_shouldReturnNull_whenCopyNotFound() {
        BookCopy result = bookCopyService.updateStatus("unknown-id", "SOLD");

        assertNull(result);
    }

    @Test
    void delete_shouldRemoveCopyFromList() {
        bookCopyService.delete("copy-1");

        assertTrue(bookCopyService.getAll().isEmpty());
    }

    @Test
    void delete_shouldDoNothing_whenIdNotFound() {
        bookCopyService.delete("unknown-id");

        assertEquals(1, bookCopyService.getAll().size());
    }
}