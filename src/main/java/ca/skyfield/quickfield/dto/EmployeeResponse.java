package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.dto.TaskRequest;
import ca.skyfield.quickfield.model.Task;

import java.util.List;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String phone,
        List<Long> taskIds
) {
}
