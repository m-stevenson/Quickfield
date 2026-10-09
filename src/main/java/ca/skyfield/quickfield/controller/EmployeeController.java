package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.EmployeeResponse;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@EnableMethodSecurity
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeResponse> getAllEmployees(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "5")
            int size,
            @RequestParam(defaultValue = "id")
            String sortBy,
            @RequestParam(defaultValue = "true")
            boolean ascending
    ){
        Sort sort = ascending ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeService.getAllEmployees(pageable);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest
    ) {
        EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable("employeeId") Long employeeId,
            @Valid @RequestBody EmployeeRequest employeeRequest
    ) {
        Long updatedEmployeeId = employeeService.updateEmployee(employeeId, employeeRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/employees/" + updatedEmployeeId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PutMapping("/{employeeId}/role")
    public ResponseEntity<?> updateEmployeeRole(
            @PathVariable("employeeId") Long employeeId,
            @Valid @RequestBody Role role
    ) {
        Long updatedEmployeeId = employeeService.updateEmployeeRole(employeeId, role);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/employees/" + updatedEmployeeId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
            employeeService.deleteEmployee(employeeId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
