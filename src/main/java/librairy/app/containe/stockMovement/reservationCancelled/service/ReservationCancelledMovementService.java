package librairy.app.containe.stockMovement.reservationCancelled.service;

import java.util.List;
import java.util.UUID;
import librairy.app.containe.bookCopy.entity.CopyStatus;
import librairy.app.containe.bookCopy.repository.BookCopyRepository;
import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.customers.repository.CustomerRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import librairy.app.containe.stockMovement.reservation.entity.ReservationMovement;
import librairy.app.containe.stockMovement.reservation.repository.ReservationMovementRepository;
import librairy.app.containe.stockMovement.reservationCancelled.entity.ReservationCancelledMovement;
import librairy.app.containe.stockMovement.reservationCancelled.repository.ReservationCancelledMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationCancelledMovementService {

  private final ReservationCancelledMovementRepository cancelledMovementRepository;
  private final ReservationMovementRepository reservationMovementRepository;
  private final BookCopyRepository bookCopyRepository;
  private final CustomerRepository customerRepository;

  public ReservationCancelledMovementService(
      ReservationCancelledMovementRepository cancelledMovementRepository,
      ReservationMovementRepository reservationMovementRepository,
      BookCopyRepository bookCopyRepository,
      CustomerRepository customerRepository) {
    this.cancelledMovementRepository = cancelledMovementRepository;
    this.reservationMovementRepository = reservationMovementRepository;
    this.bookCopyRepository = bookCopyRepository;
    this.customerRepository = customerRepository;
  }

  public ReservationCancelledMovement cancelReservation(String reservationId, String customerId) {
    validateUUID(reservationId);
    validateUUID(customerId);

    ReservationMovement originalReservation =
        reservationMovementRepository
            .findById(reservationId)
            .orElseThrow(
                () -> new NotFoundException("ReservationMovement not found: " + reservationId));

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

    // Remettre les copies en AVAILABLE
    originalReservation
        .getBookCopies()
        .forEach(
            copy -> {
              copy.setStatus(CopyStatus.AVAILABLE);
              bookCopyRepository.save(copy);
            });

    return cancelledMovementRepository.save(
        new ReservationCancelledMovement(
            originalReservation.getBook(), originalReservation, customer));
  }

  public List<ReservationCancelledMovement> getAll() {
    return cancelledMovementRepository.findAll();
  }

  public ReservationCancelledMovement getById(String id) {
    validateUUID(id);
    return cancelledMovementRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("ReservationCancelledMovement not found: " + id));
  }

  public List<ReservationCancelledMovement> getByBookId(String bookId) {
    validateUUID(bookId);
    return cancelledMovementRepository.findByBookId(bookId);
  }

  public List<ReservationCancelledMovement> getByCustomerId(String customerId) {
    validateUUID(customerId);
    return cancelledMovementRepository.findByCustomerId(customerId);
  }

  private void validateUUID(String id) {
    try {
      UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID format: " + id);
    }
  }
}
