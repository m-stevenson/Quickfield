package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable("employeeId") Long employeeId,
                                            @Valid @RequestBody EmployeeRequest employeeRequest) {
        Long updatedEmployeeId = employeeService.updateEmployee(employeeId, employeeRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/employees/" + updatedEmployeeId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
            employeeService.deleteEmployee(employeeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
