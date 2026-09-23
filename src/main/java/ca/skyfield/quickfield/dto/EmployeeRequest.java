package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.dto.TaskRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmployeeRequest(
        Long id,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Phone number is required")
        String phone,

        List<@NotNull Long> taskIds
) {
}
