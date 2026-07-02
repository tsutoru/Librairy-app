package librairy.app.containe.stockMovement.reservation.service;

import java.time.LocalDate;
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
import librairy.app.containe.stockMovement.reservation.entity.ReservationMovement;
import librairy.app.containe.stockMovement.reservation.repository.ReservationMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationMovementService {

    private final ReservationMovementRepository reservationMovementRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final CustomerRepository customerRepository;

    public ReservationMovementService(
            ReservationMovementRepository reservationMovementRepository,
            BookRepository bookRepository,
            BookCopyRepository bookCopyRepository,
            CustomerRepository customerRepository) {
        this.reservationMovementRepository = reservationMovementRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.customerRepository = customerRepository;
    }

    public ReservationMovement recordReservation(String bookId,
                                                 String customerId,
                                                 int quantity,
                                                 LocalDate expirationDate) {
        validateUUID(bookId);
        validateUUID(customerId);

        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

        List<BookCopy> availableCopies = bookCopyRepository
                .findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);

        if (availableCopies.size() < quantity) {
            throw new BadRequestException(
                    "Not enough copies available. Requested: " + quantity
                            + ", Available: " + availableCopies.size());
        }

        List<BookCopy> copiesToReserve = availableCopies.subList(0, quantity);
        copiesToReserve.forEach(copy -> {
            copy.setStatus(CopyStatus.RESERVED);
            bookCopyRepository.save(copy);
        });

        return reservationMovementRepository.save(
                new ReservationMovement(book, copiesToReserve, customer, expirationDate));
    }

    public List<ReservationMovement> getAll() {
        return reservationMovementRepository.findAll();
    }

    public ReservationMovement getById(String id) {
        validateUUID(id);
        return reservationMovementRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("ReservationMovement not found: " + id));
    }

    public List<ReservationMovement> getByBookId(String bookId) {
        validateUUID(bookId);
        return reservationMovementRepository.findByBookId(bookId);
    }

    public List<ReservationMovement> getByCustomerId(String customerId) {
        validateUUID(customerId);
        return reservationMovementRepository.findByCustomerId(customerId);
    }

    private void validateUUID(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format: " + id);
        }
    }
}