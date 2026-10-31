package ca.skyfield.quickfield.repository;

import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByOrderState(OrderState state, Pageable pageable);

    Page<Order> findByAssignedEmployeeId(Long employeeId, Pageable pageable);

}
