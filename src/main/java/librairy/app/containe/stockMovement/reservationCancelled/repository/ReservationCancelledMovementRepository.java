package librairy.app.containe.stockMovement.reservationCancelled.repository;

import java.util.List;
import librairy.app.containe.stockMovement.reservationCancelled.entity.ReservationCancelledMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationCancelledMovementRepository
    extends JpaRepository<ReservationCancelledMovement, String> {

  List<ReservationCancelledMovement> findByBookId(String bookId);

  List<ReservationCancelledMovement> findByCustomerId(String customerId);

  List<ReservationCancelledMovement> findByOriginalReservationId(String reservationId);
}
