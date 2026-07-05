package librairy.app.containe.stockMovement.sale.repository;

import java.util.List;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleMovementRepository extends JpaRepository<SaleMovement, String> {

  List<SaleMovement> findByBookId(String bookId);

  List<SaleMovement> findByCustomerId(String customerId);
}
