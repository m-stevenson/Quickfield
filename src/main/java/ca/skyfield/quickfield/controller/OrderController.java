package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.order.AssignOrderRequest;
import ca.skyfield.quickfield.dto.order.CreateOrderRequest;
import ca.skyfield.quickfield.dto.order.UpdateOrderRequest;
import ca.skyfield.quickfield.dto.order.OrderResponse;
import ca.skyfield.quickfield.model.Order;
import ca.skyfield.quickfield.model.enums.OrderState;
import ca.skyfield.quickfield.model.enums.Role;
import ca.skyfield.quickfield.service.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@EnableMethodSecurity
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponse> getAllOrders(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "5")
            int size,
            @RequestParam(defaultValue = "id")
            String sortBy,
            @RequestParam(defaultValue = "true")
            boolean ascending,
            @RequestParam(required = false)
            OrderState state
    ) {

        Sort sort = ascending ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderService.getAllOrders(pageable, state);
    }

    @PostMapping("/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);

    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PatchMapping("/{orderId}/assign")
    public ResponseEntity<OrderResponse> assignOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody AssignOrderRequest request
            ) {

        OrderResponse response =
                orderService.assignOrder(orderId, request.employeeId());

        return ResponseEntity.ok(response);

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
