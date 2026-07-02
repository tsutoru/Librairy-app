package librairy.app.containe.stockMovement.service;

import java.util.UUID;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import librairy.app.containe.stockMovement.arrival.repository.ArrivalMovementRepository;
import librairy.app.containe.stockMovement.reservation.repository.ReservationMovementRepository;
import librairy.app.containe.stockMovement.reservationCancelled.repository.ReservationCancelledMovementRepository;
import librairy.app.containe.stockMovement.sale.repository.SaleMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class StockMovementService {

  private final ArrivalMovementRepository arrivalMovementRepository;
  private final SaleMovementRepository saleMovementRepository;
  private final ReservationMovementRepository reservationMovementRepository;
  private final ReservationCancelledMovementRepository cancelledMovementRepository;
  private final BookRepository bookRepository;

  public StockMovementService(
      ArrivalMovementRepository arrivalMovementRepository,
      SaleMovementRepository saleMovementRepository,
      ReservationMovementRepository reservationMovementRepository,
      ReservationCancelledMovementRepository cancelledMovementRepository,
      BookRepository bookRepository) {
    this.arrivalMovementRepository = arrivalMovementRepository;
    this.saleMovementRepository = saleMovementRepository;
    this.reservationMovementRepository = reservationMovementRepository;
    this.cancelledMovementRepository = cancelledMovementRepository;
    this.bookRepository = bookRepository;
  }

  public int getStockByBookId(String bookId) {
    validateUUID(bookId);
    bookRepository
        .findById(bookId)
        .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

    // IN = arrivages + annulations de réservation
    int totalIn =
        arrivalMovementRepository.findByBookId(bookId).stream().mapToInt(m -> m.getQuantity()).sum()
            + cancelledMovementRepository.findByBookId(bookId).stream()
                .mapToInt(m -> m.getQuantity())
                .sum();

    // OUT = ventes + réservations
    int totalOut =
        saleMovementRepository.findByBookId(bookId).stream().mapToInt(m -> m.getQuantity()).sum()
            + reservationMovementRepository.findByBookId(bookId).stream()
                .mapToInt(m -> m.getQuantity())
                .sum();

    return totalIn - totalOut;
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
