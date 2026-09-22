package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getAllEmployeesWithTasks();

    Long updateEmployee(Long employeeId, EmployeeRequest employeeRequest);

    void deleteEmployee(Long employeeId);


}
