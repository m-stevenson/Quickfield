package ca.skyfield.quickfield.dto.order;

import ca.skyfield.quickfield.model.enums.OrderState;

import java.util.List;

public record OrderRequest(
        Long id,
        String title,
        String description,
        OrderState orderState,
        List<Long> employeeIds
) {
}
