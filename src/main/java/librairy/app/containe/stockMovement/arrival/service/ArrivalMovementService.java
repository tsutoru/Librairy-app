package librairy.app.containe.stockMovement.arrival.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.BookFormat;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import librairy.app.containe.stockMovement.arrival.entity.ArrivalMovement;
import librairy.app.containe.stockMovement.arrival.repository.ArrivalMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class ArrivalMovementService {

  private final ArrivalMovementRepository arrivalMovementRepository;
  private final BookRepository bookRepository;
  private final BookCopyRepository bookCopyRepository;

  public ArrivalMovementService(
      ArrivalMovementRepository arrivalMovementRepository,
      BookRepository bookRepository,
      BookCopyRepository bookCopyRepository) {
    this.arrivalMovementRepository = arrivalMovementRepository;
    this.bookRepository = bookRepository;
    this.bookCopyRepository = bookCopyRepository;
  }


  public ArrivalMovement recordArrival(
          String bookId,
          int quantity,
          String supplier,
          String format,
          double price) {
    validateUUID(bookId);
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));


    List<BookCopy> bookCopies = new java.util.ArrayList<>();
    for (int i = 0; i < quantity; i++) {
      BookCopy copy = new BookCopy();

      copy.setBook(book);
      copy.setStatus(CopyStatus.AVAILABLE);
      copy.setFormat(BookFormat.valueOf(format));
      copy.setPrice(price);
      bookCopies.add(bookCopyRepository.save(copy));
    }

    return arrivalMovementRepository.save(new ArrivalMovement(book, bookCopies, supplier));
  }

  public List<ArrivalMovement> getAll() {
    return arrivalMovementRepository.findAll();
  }

  public ArrivalMovement getById(String id) {
    validateUUID(id);
    return arrivalMovementRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("ArrivalMovement not found: " + id));
  }

  public List<ArrivalMovement> getByBookId(String bookId) {
    validateUUID(bookId);
    return arrivalMovementRepository.findByBookId(bookId);
  }

  public List<ArrivalMovement> getBySupplier(String supplier) {
    return arrivalMovementRepository.findBySupplier(supplier);
  }

  public void delete(String id) {
    validateUUID(id);
    getById(id);
    arrivalMovementRepository.deleteById(id);
  }

  private void validateUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID format: " + id);
    }
  }
}
