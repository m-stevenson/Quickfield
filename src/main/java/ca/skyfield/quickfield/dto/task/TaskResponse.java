package ca.skyfield.quickfield.dto.task;

import ca.skyfield.quickfield.model.enums.TaskState;

import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskState taskState,
        List<Long> employeeIds
) {
}
