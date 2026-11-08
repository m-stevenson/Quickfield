package ca.skyfield.quickfield.dto.customer;

import java.util.List;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String address,
        String email,
        List<Long> orderIds
) {
}
