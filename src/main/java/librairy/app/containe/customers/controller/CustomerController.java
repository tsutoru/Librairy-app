package librairy.app.containe.customers.controller;

import java.util.List;
import java.util.UUID;

import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.customers.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
  private final CustomerService customerService;

  @GetMapping
  public List<Customer> getAll() {
    return customerService.getAll();
  }

  @GetMapping("/{id}")
  public Customer getById(@PathVariable UUID id) {
    return customerService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Customer create(@RequestBody Customer customer) {
    return customerService.create(customer);
  }

  @PutMapping("/{id}")
  public Customer update(@PathVariable UUID id, @RequestBody Customer customer) {
    return customerService.update(id, customer);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    customerService.delete(id);
  }
}
