package ca.skyfield.quickfield.integration;

import ca.skyfield.quickfield.dto.customer.UpdateCustomerRequest;
import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class CustomerRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17");

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldFindCustomerByEmail() {

        Customer customer = Customer.builder()
                .firstName("Cust")
                .lastName("Last")
                .phone("555-111-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build();

        customerRepository.saveAndFlush(customer);

        Optional<Customer> result =
                customerRepository.findByEmail("email@test.ca");

        assertTrue(result.isPresent());
        assertEquals("email@test.ca", result.get().getEmail());
        assertEquals("Cust", result.get().getFirstName());
    }

    @Test
    void shouldUpdateCustomer() {

        Customer customer = Customer.builder()
                .firstName("Cust")
                .lastName("Last")
                .phone("555-111-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build();

        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        Long id = savedCustomer.getId();

        // Clear context to avoid reading 1st level cache
        entityManager.clear();

        Customer customerToUpdate = customerRepository.findById(id).orElseThrow();
        customerToUpdate.setFirstName("Updated");
        customerRepository.saveAndFlush(customerToUpdate);

        entityManager.clear();

        Customer updatedCustomer = customerRepository.findById(id).orElseThrow();

        assertEquals("Updated", updatedCustomer.getFirstName());
        assertEquals("Last", updatedCustomer.getLastName());
        assertEquals("555-111-0000", updatedCustomer.getPhone());
        assertEquals("123 Street Ave", updatedCustomer.getAddress());
        assertEquals("email@test.ca", updatedCustomer.getEmail());
    }

}
