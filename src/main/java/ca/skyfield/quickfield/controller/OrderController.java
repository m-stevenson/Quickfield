package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.service.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@EnableMethodSecurity
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);

    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody UpdateOrderRequest updateOrderRequest
    ) {
        Long updatedOrderId = orderService.updateOrder(orderId, updateOrderRequest).id();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/orders/" + updatedOrderId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'MANAGER', 'ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderState(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderState orderState
    ) {
        Long updatedOrderId = orderService.updateOrderState(orderId, orderState).id();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/orders/status" + updatedOrderId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("orderId") Long orderId
    ) {
        orderService.deleteOrder(orderId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
