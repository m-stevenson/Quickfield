package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.enums.Role;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getAllEmployeesWithTasks();

    Long updateEmployee(Long employeeId, EmployeeRequest employeeRequest);

    Long updateEmployeeRole(Long employeeId, Role role);

    void deleteEmployee(Long employeeId);


}
