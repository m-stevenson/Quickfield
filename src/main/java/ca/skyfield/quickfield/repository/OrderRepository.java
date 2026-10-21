package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
