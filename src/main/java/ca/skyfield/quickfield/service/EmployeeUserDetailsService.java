package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import ca.skyfield.quickfield.security.EmployeeUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new UsernameNotFoundException("Employee not found with emaiL: " + email);
        }

        return new EmployeeUserDetails(
                employee
        );
    }
}
