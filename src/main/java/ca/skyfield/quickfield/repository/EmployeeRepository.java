package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.tasks")
    List<Employee> findAllWithTasks();
}
