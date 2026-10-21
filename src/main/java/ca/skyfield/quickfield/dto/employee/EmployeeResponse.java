package ca.skyfield.quickfield.dto.employee;

import ca.skyfield.quickfield.model.enums.Role;

import java.util.List;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String email,
        Role role,
        List<Long> taskIds
) {
}
