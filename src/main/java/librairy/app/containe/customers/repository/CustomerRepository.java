package librairy.app.containe.customers.repository;

import librairy.app.containe.customers.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {}
