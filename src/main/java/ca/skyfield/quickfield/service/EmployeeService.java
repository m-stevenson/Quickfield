package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.employee.CreateEmployeeRequest;
import ca.skyfield.quickfield.dto.employee.UpdateEmployeeRequest;
import ca.skyfield.quickfield.dto.employee.EmployeeResponse;
import ca.skyfield.quickfield.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable, Role role);

    EmployeeResponse getEmployeeById(Long employeeId);

    List<EmployeeResponse> getAllEmployeesWithOrders();

    EmployeeResponse updateEmployee(Long employeeId, UpdateEmployeeRequest employeeRequest);

    Long updateEmployeeRole(Long employeeId, Role role);

    void deleteEmployee(Long employeeId);


}
