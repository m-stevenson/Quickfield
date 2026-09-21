package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.dto.TaskRequest;

import java.util.List;

public record EmployeeRequest(
        Long id,
        String firstName,
        String lastName,
        String phone,
        List<Long> taskIds
) {
}
