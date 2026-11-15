package ca.skyfield.quickfield.dto.customer;

public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        String phone,
        String address,
        String email
) {
}
