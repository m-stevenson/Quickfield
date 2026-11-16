package ca.skyfield.quickfield.integration;

import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.repository.CustomerRepository;
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

}
