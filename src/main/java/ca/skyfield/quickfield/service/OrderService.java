package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.OrderState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

        OrderResponse createOrder(CreateOrderRequest orderRequest);

        Page<OrderResponse> getAllOrders(Pageable pageable, OrderState orderState);

        OrderResponse updateOrder(Long orderId, UpdateOrderRequest updateOrderRequest);

        OrderResponse updateOrderState(Long orderId, OrderState orderState);

        OrderResponse assignOrder(Long orderId, Long employeeId);

        void deleteOrder(Long orderId);

    }



