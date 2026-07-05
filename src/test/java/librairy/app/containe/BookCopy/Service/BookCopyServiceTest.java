package librairy.app.containe.BookCopy.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.bookCopy.service.BookCopyService;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

  @Mock private BookCopyRepository bookCopyRepository;

  @InjectMocks private BookCopyService bookCopyService;

  private BookCopy copy;
  private Book book;
  private String validBookId;
  private String validCopyId;

  @BeforeEach
  void setUp() {
    validBookId = UUID.randomUUID().toString();
    validCopyId = UUID.randomUUID().toString();

    book = new Book();
    book.setId(validBookId);
    book.setTitle("Clean Code");

    copy = new BookCopy();
    copy.setId(validCopyId);
    copy.setBook(book);
    copy.setStatus(CopyStatus.AVAILABLE);
  }

  @Test
  void create_shouldSaveAndReturnCopy() {
    when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(copy);

    BookCopy result = bookCopyService.create(copy);

    assertNotNull(result);
    assertEquals(CopyStatus.AVAILABLE, result.getStatus());
    verify(bookCopyRepository, times(1)).save(copy);
  }

  @Test
  void getAll_shouldReturnAllCopies() {
    when(bookCopyRepository.findAll()).thenReturn(List.of(copy));

    List<BookCopy> result = bookCopyService.getAll();

    assertEquals(1, result.size());
    verify(bookCopyRepository, times(1)).findAll();
  }

  @Test
  void getById_shouldReturnCopy_whenExists() {
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));

    BookCopy result = bookCopyService.getById(validCopyId);

    assertNotNull(result);
    assertEquals(CopyStatus.AVAILABLE, result.getStatus());
    verify(bookCopyRepository, times(1)).findById(validCopyId);
  }

  @Test
  void getById_shouldThrowNotFoundException_whenNotFound() {
    String nonExistentId = UUID.randomUUID().toString();
    when(bookCopyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookCopyService.getById(nonExistentId));
    verify(bookCopyRepository, times(1)).findById(nonExistentId);
  }

  @Test
  void getById_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(BadRequestException.class, () -> bookCopyService.getById("invalid-uuid"));
    verify(bookCopyRepository, never()).findById(any());
  }

  @Test
  void getAvailable_shouldReturnAllAvailable_whenNoBookId() {
    when(bookCopyRepository.findByStatus(CopyStatus.AVAILABLE)).thenReturn(List.of(copy));

    List<BookCopy> result = bookCopyService.getAvailable(null);

    assertEquals(1, result.size());
    verify(bookCopyRepository, times(1)).findByStatus(CopyStatus.AVAILABLE);
  }

  @Test
  void getAvailable_shouldFilterByBookId() {
    when(bookCopyRepository.findByBookIdAndStatus(validBookId, CopyStatus.AVAILABLE))
        .thenReturn(List.of(copy));

    List<BookCopy> result = bookCopyService.getAvailable(validBookId);

    assertEquals(1, result.size());
    verify(bookCopyRepository, times(1)).findByBookIdAndStatus(validBookId, CopyStatus.AVAILABLE);
  }

  @Test
  void getStockByCopyId_shouldReturn1_whenAvailable() {
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));

    int result = bookCopyService.getStockByCopyId(validCopyId);

    assertEquals(1, result);
    verify(bookCopyRepository, times(1)).findById(validCopyId);
  }

  @Test
  void getStockByCopyId_shouldReturn0_whenNotAvailable() {
    copy.setStatus(CopyStatus.SOLD);
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));

    int result = bookCopyService.getStockByCopyId(validCopyId);

    assertEquals(0, result);
  }

  @Test
  void getStockByCopyId_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(BadRequestException.class, () -> bookCopyService.getStockByCopyId("invalid-uuid"));
    verify(bookCopyRepository, never()).findById(any());
  }

  @Test
  void updateStatus_shouldChangeStatus_whenCopyExists() {
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));
    when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(copy);

    BookCopy result = bookCopyService.updateStatus(validCopyId, "SOLD");

    assertNotNull(result);
    assertEquals(CopyStatus.SOLD, result.getStatus());
    verify(bookCopyRepository, times(1)).save(copy);
  }

  @Test
  void updateStatus_shouldThrowBadRequestException_whenInvalidStatus() {
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));

    assertThrows(
        BadRequestException.class,
        () -> bookCopyService.updateStatus(validCopyId, "INVALID_STATUS"));
  }

  @Test
  void updateStatus_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(
        BadRequestException.class, () -> bookCopyService.updateStatus("invalid-uuid", "SOLD"));
    verify(bookCopyRepository, never()).findById(any());
  }

  @Test
  void delete_shouldDeleteCopy_whenExists() {
    when(bookCopyRepository.findById(validCopyId)).thenReturn(Optional.of(copy));

    bookCopyService.delete(validCopyId);

    verify(bookCopyRepository, times(1)).deleteById(validCopyId);
  }

  @Test
  void delete_shouldThrowNotFoundException_whenNotFound() {
    String nonExistentId = UUID.randomUUID().toString();
    when(bookCopyRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookCopyService.delete(nonExistentId));
    verify(bookCopyRepository, never()).deleteById(any());
  }

  @Test
  void delete_shouldThrowBadRequestException_whenInvalidUUID() {
    assertThrows(BadRequestException.class, () -> bookCopyService.delete("invalid-uuid"));
    verify(bookCopyRepository, never()).findById(any());
  }
}
