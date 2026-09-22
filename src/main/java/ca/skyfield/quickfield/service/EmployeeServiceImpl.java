package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.Task;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        log.debug("Creating new employee {}", employeeRequest);

        Employee employee = Employee.builder()
                .firstName(employeeRequest.firstName())
                .lastName(employeeRequest.lastName())
                .phone(employeeRequest.phone())
                .build();

        employeeRepository.save(employee);

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                List.of()
        );
    }

    @Override
    public List<EmployeeResponse> getAllEmployees(){
        log.debug("Retrieving all employees");

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::mapToEmployeeResponse)
                .toList();
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                List.of()
        );
    }

    @Override
    public List<EmployeeResponse> getAllEmployeesWithTasks() {
        log.debug("Retrieving all employees with tasks");

        List<Employee> employees = employeeRepository.findAllWithTasks();

        return employees.stream()
                .map(this::mapToEmployeeResponseWithTasks)
                .toList();
    }

    private EmployeeResponse mapToEmployeeResponseWithTasks(Employee employee) {
        List<Long> taskIds = employee.getTasks() == null ? List.of() :
                employee.getTasks().stream()
                        .map(Task::getId)
                        .toList();

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                taskIds
        );
    }

    @Override
    public Long updateEmployee(Long employeeId, EmployeeRequest employeeRequest) {
        log.debug("Updating employee with id {}", employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setFirstName(employeeRequest.firstName());
        employee.setLastName(employeeRequest.lastName());
        employee.setPhone(employeeRequest.phone());

        return employeeRepository.save(employee).getId();

    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.debug("Deleting employee with id {}", employeeId);

        if (!employeeRepository.existsById(employeeId)){
            throw new EntityNotFoundException("Employee not found");
        }

        employeeRepository.deleteById(employeeId);
    }
}
