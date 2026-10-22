package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.enums.OrderState;

import java.util.List;

public interface OrderService {

        OrderResponse createOrder(CreateOrderRequest orderRequest);

        List<OrderResponse> getAllOrders();

        OrderResponse updateOrder(Long orderId, UpdateOrderRequest updateOrderRequest);

        OrderResponse updateOrderState(Long orderId, OrderState orderState);

        void deleteOrder(Long orderId);

    }



