package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.dto.TaskRequest;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.model.enums.Status;

import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Status status,
        List<Long> employeeIds
) {
}
