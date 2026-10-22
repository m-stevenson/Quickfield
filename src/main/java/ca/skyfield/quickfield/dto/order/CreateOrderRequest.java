package ca.skyfield.quickfield.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(

        @NotBlank(message = "Order title is required")
        String title,

        @NotBlank(message = "Order description is required")
        String description

        // Customer
) {
}
