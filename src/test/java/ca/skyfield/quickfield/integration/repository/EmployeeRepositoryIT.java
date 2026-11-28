package ca.skyfield.quickfield.integration.repository;

import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.CustomerRepository;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import ca.skyfield.quickfield.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
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
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

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

        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Employee employee = Employee.builder()
                    .firstName("Emp" + i)
                    .lastName("Lee" + i)
                    .phone("555-111-" + String.format("%04d", i))
                    .email("emp" + i + "@test.ca")
                    .password("12345" + i)
                    .role(Role.EMPLOYEE)
                    .build();

            employees.add(employee);
        }
        employeeRepository.saveAllAndFlush(employees);

        Pageable pageable = PageRequest.of(
                0, 5, Sort.by("firstName").ascending()
        );

        Page<Employee> result = employeeRepository.findAll(pageable);

        assertEquals(10, result.getTotalElements());
        assertEquals(5, result.getContent().size());
        assertEquals(2, result.getTotalPages());
        assertEquals("Emp1", result.getContent().get(1).getFirstName());
        assertEquals("emp4@test.ca", result.getContent().get(4).getEmail());
    }

    @Test
    void shouldEnforceUniqueEmail() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();

        Employee duplicate = Employee.builder()
                .firstName("Emp1")
                .lastName("Lee1")
                .phone("555-111-1111")
                .email("emp@test.ca")
                .password("1234565")
                .role(Role.EMPLOYEE)
                .build();

        employeeRepository.saveAndFlush(employee);

        assertThrows(DataIntegrityViolationException.class,
                () -> employeeRepository.saveAndFlush(duplicate)
        );
    }

    @Test
    void shouldFindOrdersAssignedToEmployee() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();

        Employee savedEmployee =
                employeeRepository.saveAndFlush(employee);

        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Customer")
                .email("customer@test.ca")
                .phone("555-123-4567")
                .address("123 Main Street")
                .build();

        Customer savedCustomer =
                customerRepository.saveAndFlush(customer);

        Order order = Order.builder()
                .title("Repair faucet")
                .description("Faucet leaking")
                .orderState(OrderState.ASSIGNED)
                .customer(savedCustomer)
                .assignedEmployee(savedEmployee)
                .build();

        Long orderId =
                orderRepository.saveAndFlush(order).getId();

        entityManager.clear();

        Order retrievedOrder =
                orderRepository.findById(orderId).orElseThrow();

        assertNotNull(retrievedOrder.getAssignedEmployee());
        assertEquals(savedEmployee.getId(), retrievedOrder.getAssignedEmployee().getId());
    }

    @Test
    void shouldDeleteEmployee() {

        Employee employee = Employee.builder()
                .firstName("Emp")
                .lastName("Lee")
                .phone("555-111-0000")
                .email("emp@test.ca")
                .password("12345")
                .role(Role.EMPLOYEE)
                .build();

        employeeRepository.saveAndFlush(employee);

        employeeRepository.delete(employee);

        Optional<Employee> deletedEmployee =
                employeeRepository.findById(employee.getId());

        assertEquals(Optional.empty(), deletedEmployee);
    }

}
