package librairy.app.containe.stockMovement.reservation.repository;

import librairy.app.containe.stockMovement.reservation.entity.ReservationMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationMovementRepository
        extends JpaRepository<ReservationMovement, String> {

    List<ReservationMovement> findByBookId(String bookId);
    List<ReservationMovement> findByCustomerId(String customerId);
}