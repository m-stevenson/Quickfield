package ca.skyfield.quickfield.integration.repository;

import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
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
public class EmployeeRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17");

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void shouldFindEmployeeById() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();
        Long id = employeeRepository.saveAndFlush(employee).getId();
        entityManager.clear();

        Employee employeeToFind = employeeRepository.findById(id).orElseThrow();

        assertNotNull(id);
        assertEquals("Emp", employeeToFind.getFirstName());
        assertEquals("emp@test.ca", employeeToFind.getEmail());
        assertEquals(Role.EMPLOYEE, employeeToFind.getRole());
    }

    @Test
    void shouldFindEmployeeByEmail() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();
        employeeRepository.saveAndFlush(employee);

        Optional<Employee> result =
                employeeRepository.findByEmail("emp@test.ca");

        assertTrue(result.isPresent());
        assertEquals("Emp", result.get().getFirstName());
        assertEquals("emp@test.ca", result.get().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {

        String email = "false@test.ca";

        Optional<Employee> employee = employeeRepository.findByEmail(email);

        assertEquals(Optional.empty(), employee);
    }

    @Test
    void shouldUpdateEmployee() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();

        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        Long id = savedEmployee.getId();
        entityManager.clear();

        Employee employeeToUpdate = employeeRepository.findById(id).orElseThrow();
        employeeToUpdate.setFirstName("Updated");
        employeeRepository.saveAndFlush(employeeToUpdate);
        entityManager.clear();

        Employee updatedEmployee = employeeRepository.findById(id).orElseThrow();

        assertEquals("Updated", updatedEmployee.getFirstName());
        assertEquals("Lee", updatedEmployee.getLastName());
        assertEquals("555-111-0000", updatedEmployee.getPhone());
        assertEquals("emp@test.ca", updatedEmployee.getEmail());
        assertEquals(Role.EMPLOYEE, updatedEmployee.getRole());
    }

    @Test
    void shouldFindAllEmployeesWithPagination() {


    }

    @Test
    void shouldEnforceUniqueEmail() {

    }

    @Test
    void shouldPersistEmployeeRole() {

    }

    @Test
    void shouldFindOrdersAssignedToEmployee() {

    }

    @Test
    void shouldDeleteEmployee() {

    }


}
