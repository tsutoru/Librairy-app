package librairy.app.containe.stockMovement.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StockMovementService {

  private final BookRepository bookRepository;
  private final BookCopyRepository bookCopyRepository;

  public StockMovementService(
      BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
    this.bookRepository = bookRepository;
    this.bookCopyRepository = bookCopyRepository;
  }

  public int getStockByBookId(String bookId) {

    validateUUID(bookId);

    bookRepository
        .findById(bookId)
        .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

    return (int) bookCopyRepository.countByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);
  }

  public List<Map<String, Object>> getLowStockBooks(int threshold) {
    return bookRepository.findAll().stream()
        .map(
            book -> {
              int stock = getStockByBookId(book.getId());
              Map<String, Object> result = new java.util.HashMap<>();
              result.put("book", book);
              result.put("stock", stock);
              return result;
            })
        .filter(map -> (int) map.get("stock") <= threshold)
        .toList();
  }

  public boolean isAvailable(String bookId) {
    return getStockByBookId(bookId) > 0;
  }

  private void validateUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID format: " + id);
    }
  }
}
