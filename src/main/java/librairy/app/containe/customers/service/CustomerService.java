package librairy.app.containe.customers.service;

import java.util.List;
import java.util.UUID;

import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.customers.repository.CustomerRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;

  public List<Customer> getAll() {
    return customerRepository.findAll();
  }

  public Customer getById(UUID id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
  }

  public Customer create(Customer customer) {
    if (customer.getEmail() == null || customer.getEmail().isBlank()) {
      throw new BadRequestException("Email is required");
    }
    return customerRepository.save(customer);
  }

  public Customer update(UUID id, Customer updated) {
    Customer existing = getById(id);
    existing.setFirstName(updated.getFirstName());
    existing.setLastName(updated.getLastName());
    existing.setEmail(updated.getEmail());
    existing.setPhone(updated.getPhone());
    existing.setAddress(updated.getAddress());
    return customerRepository.save(existing);
  }

  public void delete(UUID id) {
    customerRepository.deleteById(id);
  }
}
