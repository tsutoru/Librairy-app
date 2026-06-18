package librairy.app.containe.customers.repository;

import java.util.Optional;
import java.util.UUID;

import librairy.app.containe.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  Optional<Customer> findByEmail(String email);
}
