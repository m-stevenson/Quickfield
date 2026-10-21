package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.employee.CreateEmployeeRequest;
import ca.skyfield.quickfield.dto.employee.EmployeeResponse;
import ca.skyfield.quickfield.dto.employee.UpdateEmployeeRequest;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest) {
        log.info("Creating new employee {}", employeeRequest);

        Employee employee = Employee.builder()
                .firstName(employeeRequest.firstName())
                .lastName(employeeRequest.lastName())
                .phone(employeeRequest.phone())
                .role(Role.EMPLOYEE)    // Default role on creation
                .build();

        employeeRepository.save(employee);

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getRole(),
                List.of()
        );
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable, Role role){

        Page<Employee> employees;

        if (role != null) {
            log.info("Retrieving page {} of employees with role {}", pageable.getPageNumber(), role.name());

            employees = employeeRepository.findByRole(role, pageable);
        } else {
            log.info("Retrieving page {} of all employees", pageable.getPageNumber());

            employees = employeeRepository.findAll(pageable);
        }

        return employees.map(this::mapToEmployeeResponse);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        return mapToEmployeeResponse(employee);

    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getRole(),
                List.of()
        );
    }

    @Override
    public List<EmployeeResponse> getAllEmployeesWithOrders() {
        log.info("Retrieving all employees with orders");

        List<Employee> employees = employeeRepository.findAllWithOrders();

        return employees.stream()
                .map(this::mapToEmployeeResponseWithOrders)
                .toList();
    }

    private EmployeeResponse mapToEmployeeResponseWithOrders(Employee employee) {
        List<Long> orderIds = employee.getOrders() == null ? List.of() :
                employee.getOrders().stream()
                        .map(Order::getId)
                        .toList();

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getRole(),
                orderIds
        );
    }

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, UpdateEmployeeRequest employeeRequest) {
        log.info("Updating employee with id {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employeeRequest.firstName() != null) {
            employee.setFirstName(employeeRequest.firstName());
        }
        if (employeeRequest.lastName() != null) {
            employee.setLastName(employeeRequest.lastName());
        }
        if (employeeRequest.phone() != null) {
            employee.setPhone(employeeRequest.phone());
        }
        if (employeeRequest.email() != null) {
            employee.setEmail(employeeRequest.email());
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToEmployeeResponse(savedEmployee);

    }

    @Override
    public Long updateEmployeeRole(Long employeeId, Role role) {
        log.info("Updating employee with id {} to role of {}", employeeId, role);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employee.getRole() == role) {
            throw new IllegalStateException("Employee already has this role");
        }

        employee.setRole(role);

        return employeeRepository.save(employee).getId();

    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Deleting employee with id {}", employeeId);

        if (!employeeRepository.existsById(employeeId)){
            throw new EntityNotFoundException("Employee not found");
        }

        employeeRepository.deleteById(employeeId);
    }
}
