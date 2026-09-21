package ca.skyfield.quickfield.dto;

import ca.skyfield.quickfield.model.enums.Status;

import java.util.List;

public record TaskRequest(
        Long id,
        String title,
        String description,
        Status status,
        List<Long> employeeIds
) {
}
