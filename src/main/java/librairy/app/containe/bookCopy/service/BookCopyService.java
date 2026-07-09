package librairy.app.containe.bookCopy.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

  private final BookCopyRepository bookCopyRepository;

  public BookCopyService(BookCopyRepository bookCopyRepository) {
    this.bookCopyRepository = bookCopyRepository;
  }

  public BookCopy create(BookCopy copy) {
    try {
      return bookCopyRepository.save(copy);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("BookCopy data is invalid: " + e.getMessage());
    }
  }

  public List<BookCopy> getAll() {
    return bookCopyRepository.findAll();
  }

  public BookCopy getById(String id) {
    validateUUID(id);
    return bookCopyRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("BookCopy with id " + id + " not found"));
  }

  public List<BookCopy> getAvailable(String bookId) {

    if (bookId != null) {
      validateUUID(bookId);

      return bookCopyRepository.findByBookIdAndStatus(
              bookId,
              CopyStatus.AVAILABLE
      );
    }

    return bookCopyRepository.findByStatus(
            CopyStatus.AVAILABLE
    );
  }

  public int getStockByCopyId(String copyId) {
    validateUUID(copyId);
    BookCopy copy = getById(copyId);
    return copy.getStatus() == CopyStatus.AVAILABLE ? 1 : 0;
  }

  public BookCopy updateStatus(String id, String status) {
    validateUUID(id);
    BookCopy copy = getById(id);
    try {
      copy.setStatus(CopyStatus.valueOf(status));
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid status: " + status);
    }
    return bookCopyRepository.save(copy);
  }

  public void delete(String id) {
    validateUUID(id);
    getById(id);
    bookCopyRepository.deleteById(id);
  }

  private void validateUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(
          "Invalid UUID format: " + id + ". UUID must be a valid 36-character string.");
    }
  }

  public List<BookCopy> getByBookAndStatus(
          String bookId,
          CopyStatus status
  ) {
    validateUUID(bookId);

    return bookCopyRepository.findByBookIdAndStatus(
            bookId,
            status
    );
  }
}
