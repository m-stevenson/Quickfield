package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void shouldReturnEmployeeWhenIdExists() {

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Employee")
                .email("test@test.ca")
                .phone("566-344-1233")
                .role(Role.EMPLOYEE)
                .build();

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse result =
                employeeService.getEmployeeById(1L);

        assertEquals(1L, result.id());
        assertEquals("Test", result.firstName());
        assertEquals("Employee", result.lastName());
    }

    @Test
    void shouldCreateEmployee() {

        EmployeeRequest request =
                new EmployeeRequest(
                        1L,
                        "Test",
                        "Employee",
                        "111-111-1111",
                        "test@test.ca",
                        List.of()
                );

        when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(invocation -> {
                    Employee employee = invocation.getArgument(0);
                    employee.setId(1L);
                    return employee;
                });

        employeeService.createEmployee(request);

        ArgumentCaptor<Employee> captor =
                ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository)
                .save(captor.capture());

        Employee savedEmployee = captor.getValue();

        assertEquals(1L, savedEmployee.getId());
        assertEquals("Test", savedEmployee.getFirstName());
    }

    @Test
    void shouldCreateEmployeeWithDefaultRole() {

        EmployeeRequest request =
                new EmployeeRequest(
                        1L,
                        "Test",
                        "Employee",
                        "111-111-1111",
                        "test@test.ca",
                        List.of()
                );

        when(employeeRepository.save(Mockito.any(Employee.class)))
                .thenAnswer(invocation -> {
                    Employee employee = invocation.getArgument(0);
                    employee.setId(1L);
                    return employee;
                });

        employeeService.createEmployee(request);

        ArgumentCaptor<Employee> captor =
                ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository)
                .save(captor.capture());

        Employee savedEmployee = captor.getValue();

        assertEquals(Role.EMPLOYEE, savedEmployee.getRole());
        assertEquals("Test", savedEmployee.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(192L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> employeeService.getEmployeeById(192L)
        );
    }

}
