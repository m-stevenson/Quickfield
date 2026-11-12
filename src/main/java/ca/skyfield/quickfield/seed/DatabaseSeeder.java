package ca.skyfield.quickfield.seed;

import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.CustomerRepository;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import ca.skyfield.quickfield.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        seedEmployees();
        seedCustomers();
        seedOrders();
    }

    private void seedEmployees() {

            if (!employeeRepository.existsByEmail("admin@test.ca")) {
                log.info("Registering employee with admin role");

                Employee adminEmployee = Employee.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .password(encoder.encode("password"))
                        .email("admin@test.ca")
                        .phone("647-555-0000")
                        .role(Role.ADMIN)
                        .build();
                employeeRepository.save(adminEmployee);
            }

            log.info("Seeding database with 50 employees...");
            List<Employee> employees = new ArrayList<>();
            for (int i = 1; i <= 50; i++){
                String email = "employee" + i + "@test.ca";

                if (employeeRepository.existsByEmail(email)) {
                    continue;
                }

                Employee employee = Employee.builder()
                        .firstName("Employee" + i)
                        .lastName("Test" + i)
                        .password(encoder.encode("password"))
                        .email(email)
                        .phone("647-555-" + String.format("%04d", i))
                        .role(Role.EMPLOYEE)
                        .build();

                employees.add(employee);
            }

            employeeRepository.saveAll(employees);

            log.info("Seeded {} employees", employees.size());
        }

    private void seedCustomers() {
            log.info("Seeding database with 20 customers...");
            List<Customer> customers = new ArrayList<>();
            for (int i = 1; i <= 20; i++){

                String email = "customer" + i + "@test.ca";

                if (customerRepository.existsByEmail(email)) {
                    continue;
                }

                Customer customer = Customer.builder()
                        .firstName("Customer" + i)
                        .lastName("The" + i + "rd")
                        .phone("647-555-" + String.format("%04d", i))
                        .address(i + "00 " + "Avenue")
                        .email(email)
                        .build();

                customers.add(customer);
            }

            customerRepository.saveAll(customers);

            log.info("Seeded {} customers", customers.size());
        }

        private void seedOrders() {

            if (orderRepository.count() > 0) {
                return;
            }

            log.info("Seeding database with 50 orders...");

            List<Customer> customers = customerRepository.findAll();

            List<Employee> employees = employeeRepository.findAll();

            List<Order> orders = new ArrayList<>();
            for (int i = 1; i <= 50; i++){

                Customer customer =
                        customers.get((i - 1) % customers.size());

                Employee employee =
                        employees.get((i - 1) % employees.size());

                Order order = Order.builder()
                        .title("Maintenance Request " + i)
                        .description("Case #" + i)
                        .orderState(i <= 25 ? OrderState.ASSIGNED : OrderState.NEW)
                        .customer(customer)
                        .assignedEmployee(i <= 25 ? employee : null)
                        .build();

                orders.add(order);
            }

            orderRepository.saveAll(orders);

            log.info("Seeded {} orders", orders.size());
        }




    }
