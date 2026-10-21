package ca.skyfield.quickfield.dto.employee;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmployeeRequest(
        String firstName,
        String lastName,
        String phone,
        String email,
        List<@NotNull Long> taskIds
) {
}
