package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.model.enums.TaskState;

import java.util.List;

public record TaskRequest(
        Long id,
        String title,
        String description,
        TaskState taskState,
        List<Long> employeeIds
) {
}
