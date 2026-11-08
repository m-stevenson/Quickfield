package ca.skyfield.quickfield.controller;

import ca.skyfield.quickfield.dto.customer.CustomerResponse;
import ca.skyfield.quickfield.dto.customer.UpdateCustomerRequest;
import ca.skyfield.quickfield.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/customers")
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CustomerResponse> getAllCustomers(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "5")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return customerService.getAllCustomers(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getCustomerById(
            @PathVariable("customerId") Long customerId
    ) {
        return customerService.getCustomerById(customerId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateCustomer(
            @PathVariable("customerId") Long customerId,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        Long updatedCustomerId = customerService.updateCustomer(customerId, request).id();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/employees/" + updatedCustomerId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


}
