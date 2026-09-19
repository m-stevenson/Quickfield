package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
