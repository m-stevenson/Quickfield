package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.OrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.debug("Creating new order {}", orderRequest.id());

        Order order = Order.builder()
                .title(orderRequest.title())
                .description(orderRequest.description())
                .orderState(OrderState.TODO)
                .build();

        return new OrderResponse(
                order.getId(),
                order.getTitle(),
                order.getDescription(),
                order.getOrderState(),
                List.of()
        );
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.debug("Retrieving all orders");

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
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

    @Override
    public Long updateOrder(Long orderId, OrderRequest orderRequest) {
        log.debug("Updating order with id {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setTitle(orderRequest.title());
        order.setDescription(orderRequest.description());
        order.setOrderState(orderRequest.orderState());

        return orderRepository.save(order).getId();
    }

    @Override
    public Long updateOrderState(Long orderId, OrderState orderState) {
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

        return orderRepository.save(order).getId();
    }

    @Override
    public void deleteOrder(Long orderId) {
        log.debug("Deleting order with id {}", orderId);

        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found");
        }

        orderRepository.deleteById(orderId);
    }
}
