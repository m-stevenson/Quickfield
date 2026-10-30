package ca.skyfield.quickfield.dto.order;

import ca.skyfield.quickfield.dto.customer.CustomerRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(

        @NotBlank(message = "Order title is required")
        String title,

        @NotBlank(message = "Order description is required")
        String description,

        @NotNull(message = "Customer information is required")
        CustomerRequest customer
) {
}
