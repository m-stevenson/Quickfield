package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.customer.CustomerRequest;
import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.repository.CustomerRepository;
import ca.skyfield.quickfield.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    void shouldCreateOrder() {

        CustomerRequest customer = new CustomerRequest(
                "Customer",
                "Test",
                "555-555-0000",
                "12 Street Ave",
                "customer@test.ca"
        );

        CreateOrderRequest orderRequest =
                new CreateOrderRequest(
                        "Order 1",
                        "order description",
                        customer
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
                .orderState(OrderState.NEW)
                .build());

        orders.add(Order.builder()
                .id(2L)
                .title("Order 2")
                .description("order 2 desc")
                .orderState(OrderState.NEW)
                .build());

        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> orderPage =
                new PageImpl<>(orders, pageable, 2);

        when(orderRepository.findAll(pageable))
                .thenReturn(orderPage);

        Page<OrderResponse> result =
                orderService.getAllOrders(pageable, null);

        verify(orderRepository)
                .findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals("Order 2", result.getContent().get(1).title());
        assertEquals("Order 1", result.getContent().get(0).title());
        assertEquals(OrderState.NEW, result.getContent().get(1).orderState());
    }

    @Test
    void shouldGetOrdersFilteredByState() {

        Order order = Order.builder()
                .id(1L)
                .title("Order 1")
                .orderState(OrderState.NEW)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> orderPage =
                new PageImpl<>(List.of(order), pageable, 1);

        when(orderRepository.findByOrderState(OrderState.NEW, pageable))
                .thenReturn(orderPage);

        Page<OrderResponse> result =
                orderService.getAllOrders(pageable, OrderState.NEW);

        verify(orderRepository)
                .findByOrderState(OrderState.NEW, pageable);

        // Verify state was passed
        verify(orderRepository, never())
                .findAll(any(Pageable.class));

        assertEquals(1, result.getTotalElements());
        assertEquals(OrderState.NEW, result.getContent().get(0).orderState());
    }

    @Test
    void shouldReturnEmptyPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Order> orderPage =
                new PageImpl<>(List.of(), pageable, 0);

        when(orderRepository.findAll(pageable))
                .thenReturn(orderPage);

        Page<OrderResponse> result =
                orderService.getAllOrders(pageable, null);

        verify(orderRepository).findAll(pageable);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void shouldCreateOrderWithExistingCustomer() {

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Customer first")
                .lastName("last")
                .phone("555-555-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build();

        when(customerRepository.findByEmail("email@test.ca"))
                .thenReturn(Optional.of(customer));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderRequest request = new CreateOrderRequest(
                "Title",
                "desc",
                new CustomerRequest(
                        ("Customer first"),
                        ("last"),
                        ("555-555-0000"),
                        ("123 Street Ave"),
                        ("email@test.ca")
                        ));

        orderService.createOrder(request);

        verify(orderRepository).save(orderCaptor.capture());

        assertEquals(customer, orderCaptor.getValue().getCustomer());

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void shouldCreateOrderWithNewCustomer() {

        when(customerRepository.findByEmail("email@test.ca"))
                .thenReturn(Optional.empty());

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> {
                    Customer customer = invocation.getArgument(0);
                    customer.setId(1L);
                    return customer;
                });

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderRequest request = new CreateOrderRequest(
                "Title",
                "desc",
                new CustomerRequest(
                        ("Customer first"),
                        ("last"),
                        ("555-555-0000"),
                        ("123 Street Ave"),
                        ("email@test.ca")
                ));

        orderService.createOrder(request);

        verify(orderRepository)
                .save(orderCaptor.capture());

        verify(customerRepository)
                .save(customerCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        Customer savedCustomer = customerCaptor.getValue();

        assertEquals("Title", savedOrder.getTitle());
        assertEquals(OrderState.NEW, savedOrder.getOrderState());
        assertEquals("email@test.ca", savedCustomer.getEmail());
        assertEquals(1L, savedCustomer.getId());
        assertEquals("Customer first", savedCustomer.getFirstName());
        assertEquals(savedCustomer, savedOrder.getCustomer());
    }

    @Test
    void shouldUpdateOrder() {

        Order order = Order.builder()
                .id(1L)
                .title("Order Title")
                .description("order desc")
                .orderState(OrderState.NEW)
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
        assertEquals(OrderState.NEW, result.orderState());
        assertNotEquals("order desc", result.description());
    }

    @Test
    void shouldUpdateOrderState() {

        Order order = Order.builder()
                .id(1L)
                .title("Order Title")
                .description("order desc")
                .orderState(OrderState.NEW)
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderState state = OrderState.ASSIGNED;

        OrderResponse result =
                orderService.updateOrderState(1L, state);

        assertEquals(OrderState.ASSIGNED, result.orderState());
    }

    @Test
    void shouldDeleteOrder() {

        when(orderRepository.existsById(1L))
                .thenReturn(true);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

}
