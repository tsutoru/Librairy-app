package librairy.app.containe.stockMovement.arrival.repository;

import java.util.List;
import librairy.app.containe.stockMovement.arrival.entity.ArrivalMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalMovementRepository extends JpaRepository<ArrivalMovement, String> {

  List<ArrivalMovement> findByBookId(String bookId);

  List<ArrivalMovement> findBySupplier(String supplier);
}
