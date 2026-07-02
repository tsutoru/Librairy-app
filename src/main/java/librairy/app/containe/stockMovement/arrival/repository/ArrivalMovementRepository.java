package librairy.app.containe.stockMovement.arrival.repository;

import librairy.app.containe.stockMovement.arrival.entity.ArrivalMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArrivalMovementRepository
        extends JpaRepository<ArrivalMovement, String> {

    List<ArrivalMovement> findByBookId(String bookId);
    List<ArrivalMovement> findBySupplier(String supplier);
}