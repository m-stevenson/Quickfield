package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
