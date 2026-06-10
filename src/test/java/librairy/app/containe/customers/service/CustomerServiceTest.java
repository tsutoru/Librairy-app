package librairy.app.containe.customers.service;

import librairy.app.containe.customers.entity.Customer;
import librairy.app.containe.customers.repository.CustomerRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId("uuid-123");
        customer.setFirstName("Alice");
        customer.setLastName("Dupont");
        customer.setEmail("alice.dupont@gmail.com");
        customer.setPhone("+261341234567");
        customer.setAddress("Lot 123 Antananarivo");
    }

    @Test
    void getAll() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAll();

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void getById() {
        when(customerRepository.findById("uuid-123")).thenReturn(Optional.of(customer));

        Customer result = customerService.getById("uuid-123");

        assertEquals("alice.dupont@gmail.com", result.getEmail());
        verify(customerRepository).findById("uuid-123");
    }

    @Test
    void getById_notFound() {
        when(customerRepository.findById("uuid-999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getById("uuid-999"));
    }

    @Test
    void create() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.create(customer);

        assertEquals("Alice", result.getFirstName());
        verify(customerRepository).save(customer);
    }

    @Test
    void create_emailNull() {
        customer.setEmail(null);

        assertThrows(BadRequestException.class, () -> customerService.create(customer));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void update() {
        Customer updated = new Customer();
        updated.setFirstName("Bob");
        updated.setLastName("Martin");
        updated.setEmail("bob.martin@gmail.com");
        updated.setPhone("+261340000000");
        updated.setAddress("Lot 456 Fianarantsoa");

        when(customerRepository.findById("uuid-123")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.update("uuid-123", updated);

        assertEquals("Bob", result.getFirstName());
        verify(customerRepository).save(customer);
    }

    @Test
    void delete() {
        customerService.delete("uuid-123");

        verify(customerRepository).deleteById("uuid-123");
    }
}
