package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.EmployeeRequest;
import ca.skyfield.quickfield.dto.LoginRequest;
import ca.skyfield.quickfield.dto.RegisterRequest;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import ca.skyfield.quickfield.security.JwtUtil;
import ca.skyfield.quickfield.service.EmployeeService;
import ca.skyfield.quickfield.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder encoder;
    private final EmployeeServiceImpl employeeService;
    private final JwtUtil jwtUtils;

    @PostMapping("/login")
    public String authenticateEmployee(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        UserDetails details = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(details.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(
            @RequestBody @Valid RegisterRequest request
    ) {

        if (employeeRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already in use");
        }

        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setPhone(request.phone());
        employee.setEmail(request.email());
        employee.setPassword(encoder.encode(request.password()));

        employee.setRole(Role.EMPLOYEE);  // Default role on registration

        employeeRepository.save(employee);

        return ResponseEntity.ok("Employee registered successfully");
    }

}
