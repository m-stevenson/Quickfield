package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    void shouldCreateOrder() {

        CreateOrderRequest orderRequest =
                new CreateOrderRequest(
                        "Order 1",
                        "order description"
                );

        when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(1L);
                    return order;
                });

        orderService.createOrder(orderRequest);

        verify(orderRepository)
                .save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertEquals(1L, savedOrder.getId());
        assertEquals("Order 1", savedOrder.getTitle());
        assertEquals("order description", savedOrder.getDescription());

    }

    @Test
    void shouldGetAllOrders() {

        List<Order> orders =
                new ArrayList<>();

        orders.add(Order.builder()
                .id(1L)
                .title("Order 1")
                .description("order 1 desc")
                .orderState(OrderState.TODO)
                .build());

        orders.add(Order.builder()
                .id(2L)
                .title("Order 2")
                .description("order 2 desc")
                .orderState(OrderState.TODO)
                .build());

        when(orderRepository.findAll())
                .thenReturn(orders);

        List<OrderResponse> result = orderService.getAllOrders();

        verify(orderRepository).findAll();

        assertEquals(2, result.size());
        assertEquals("Order 2", result.get(1).title());
        assertEquals(OrderState.TODO, result.get(1).orderState());

    }

    @Test
    void shouldUpdateOrder() {

        Order order = Order.builder()
                .id(1L)
                .title("Order Title")
                .description("order desc")
                .orderState(OrderState.TODO)
                .build();

        UpdateOrderRequest request =
                new UpdateOrderRequest(
                        null,
                        "Updated desc",
                        null,
                        null
                );

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse result =
                orderService.updateOrder(1L, request);

        assertEquals(1L, result.id());
        assertEquals("Order Title", result.title());
        assertEquals("Updated desc", result.description());
        assertEquals(OrderState.TODO, result.orderState());
        assertNotEquals("order desc", result.description());

    }

    @Test
    void shouldUpdateOrderState() {

        Order order = Order.builder()
                .id(1L)
                .title("Order Title")
                .description("order desc")
                .orderState(OrderState.TODO)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderState state = OrderState.IN_PROGRESS;

        OrderResponse result =
                orderService.updateOrderState(1L, state);

        assertEquals(OrderState.IN_PROGRESS, result.orderState());

    }

    @Test
    void shouldDeleteOrder() {

        when(orderRepository.existsById(1L))
                .thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);

    }

}
