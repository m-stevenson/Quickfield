package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
