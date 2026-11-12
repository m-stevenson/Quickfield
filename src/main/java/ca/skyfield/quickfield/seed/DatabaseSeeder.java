package ca.skyfield.quickfield.seed;

import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
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
public class EmployeeSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {

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
                Employee employee = Employee.builder()
                        .firstName("Employee" + i)
                        .lastName("Test" + i)
                        .password(encoder.encode("password"))
                        .email("employee" + i + "@test.ca")
                        .phone("647-555-" + String.format("%04d", i))
                        .role(Role.EMPLOYEE)
                        .build();

                employees.add(employee);
            }

            employeeRepository.saveAll(employees);

            log.info("Seeded {} employees", employees.size());

        }
    }
}
