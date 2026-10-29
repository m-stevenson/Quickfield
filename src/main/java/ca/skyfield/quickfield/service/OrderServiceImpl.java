package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.repository.EmployeeRepository;
import ca.skyfield.quickfield.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        log.debug("Creating new order {}", orderRequest);

        Order order = Order.builder()
                .title(orderRequest.title())
                .description(orderRequest.description())
                .orderState(OrderState.NEW) // Default order state
                .build();

        orderRepository.save(order);

        return mapToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable, OrderState state) {

        Page<Order> orders;

        if (state != null) {
            log.info("Retrieving page {} of orders with state {}", pageable.getPageNumber(), state.name());

            orders = orderRepository.findByOrderState(state, pageable);
        } else {
            log.info("Retrieving page {} of orders", pageable.getPageNumber());

            orders = orderRepository.findAll(pageable);
        }

        return orders.map(this::mapToOrderResponse);
    }

    @Override
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest orderRequest) {
        log.debug("Updating order with id {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (orderRequest.title() != null) {
            order.setTitle(orderRequest.title());
        }
        if (orderRequest.description() != null) {
            order.setDescription(orderRequest.description());
        }

        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    @Override
    public OrderResponse updateOrderState(Long orderId, OrderState orderState) {
        log.debug("Updating order with id {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderState currentState = order.getOrderState();
        if (!currentState.canTransitionTo(orderState)) {
            throw new IllegalStateException(
                    String.format("Invalid transition from %s to %s", currentState, orderState)
            );
        }

        order.setOrderState(orderState);

        return mapToOrderResponse(order);
    }

    @Override
    public OrderResponse assignOrder(Long orderId, Long employeeId) {
        log.info("Assigning order {} to employee with id {}", orderId, employeeId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (employee.getOrders().contains(order)) {
            throw new IllegalStateException("Employee already has order with id " + orderId);
        }

        order.setOrderState(OrderState.ASSIGNED);
        employee.getOrders().add(order);

        employeeRepository.save(employee);

        return mapToOrderResponse(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        log.debug("Deleting order with id {}", orderId);

        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found");
        }

        orderRepository.deleteById(orderId);
    }

    private OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTitle(),
                order.getDescription(),
                order.getOrderState(),
                List.of()
        );
    }
}
