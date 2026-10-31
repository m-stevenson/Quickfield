package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.orders")
    List<Employee> findAllWithOrders();

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Employee> findByRole(Role role, Pageable pageable);
}
