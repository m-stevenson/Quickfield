package ca.skyfield.quickfield.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String phone,

        @NotBlank
        String address,

        @NotBlank
        String email
) {
}
