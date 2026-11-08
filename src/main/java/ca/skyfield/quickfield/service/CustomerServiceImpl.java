package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.customer.CustomerResponse;
import ca.skyfield.quickfield.dto.customer.UpdateCustomerRequest;
import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.model.Employee;
import ca.skyfield.quickfield.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        log.info("Retrieving page {} of customers", pageable.getPageNumber());

        Page<Customer> customers = customerRepository.findAll(pageable);

        return customers.map(this::mapToCustomerResponse);

    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("Retrieving customer by id {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return mapToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with id {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }

        Customer savedCustomer = customerRepository.save(customer);

        return mapToCustomerResponse(savedCustomer);
    }

    private CustomerResponse mapToCustomerResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getEmail(),
                List.of()
        );
    }

}
