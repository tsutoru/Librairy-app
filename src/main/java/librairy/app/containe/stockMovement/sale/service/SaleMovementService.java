package librairy.app.containe.stockMovement.sale.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.bookCopy.entity.BookCopy;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.customers.repository.CustomerRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import librairy.app.containe.stockMovement.sale.repository.SaleMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class SaleMovementService {

  private final SaleMovementRepository saleMovementRepository;
  private final BookRepository bookRepository;
  private final BookCopyRepository bookCopyRepository;
  private final CustomerRepository customerRepository;

  public SaleMovementService(
      SaleMovementRepository saleMovementRepository,
      BookRepository bookRepository,
      BookCopyRepository bookCopyRepository,
      CustomerRepository customerRepository) {
    this.saleMovementRepository = saleMovementRepository;
    this.bookRepository = bookRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.customerRepository = customerRepository;
  }

  public SaleMovement recordSale(String bookId, String customerId, int quantity) {
    validateUUID(bookId);
    validateUUID(customerId);

    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

    // Récupérer N copies AVAILABLE
    List<BookCopy> availableCopies =
        bookCopyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);

    if (availableCopies.size() < quantity) {
      throw new BadRequestException(
          "Not enough copies available. Requested: "
              + quantity
              + ", Available: "
              + availableCopies.size());
    }

    // Prendre les N premières copies et les marquer SOLD
    List<BookCopy> copiesToSell = availableCopies.subList(0, quantity);
    copiesToSell.forEach(
        copy -> {
          copy.setStatus(CopyStatus.SOLD);
          bookCopyRepository.save(copy);
        });

    return saleMovementRepository.save(new SaleMovement(book, copiesToSell, customer));
  }

  public List<SaleMovement> getAll() {
    return saleMovementRepository.findAll();
  }

  public SaleMovement getById(String id) {
    validateUUID(id);
    return saleMovementRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("SaleMovement not found: " + id));
  }

  public List<SaleMovement> getByBookId(String bookId) {
    validateUUID(bookId);
    return saleMovementRepository.findByBookId(bookId);
  }

  public List<SaleMovement> getByCustomerId(String customerId) {
    validateUUID(customerId);
    return saleMovementRepository.findByCustomerId(customerId);
  }

  private void validateUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID format: " + id);
    }
  }
}
