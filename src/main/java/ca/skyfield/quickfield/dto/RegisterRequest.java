package ca.skyfield.quickfield.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotBlank(message = "Email address is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(
                min = 6,
                max = 32,
                message = "Password must be between 6 and 32 characters"
        )
        String password
) {
}
