package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.customer.CustomerResponse;
import ca.skyfield.quickfield.dto.customer.UpdateCustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

}
