package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    List<EmployeeResponse> getAllEmployeesWithTasks();

    Long updateEmployee(Long employeeId, EmployeeRequest employeeRequest);

    Long updateEmployeeRole(Long employeeId, Role role);

    void deleteEmployee(Long employeeId);


}
