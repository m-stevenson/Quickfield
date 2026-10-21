package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.OrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.enums.OrderState;

import java.util.List;

public interface OrderService {

        OrderResponse createOrder(OrderRequest orderRequest);

        List<OrderResponse> getAllOrders();

        Long updateOrder(Long orderId, OrderRequest orderRequest);

        Long updateOrderState(Long orderId, OrderState orderState);

        void deleteOrder(Long orderId);

    }



