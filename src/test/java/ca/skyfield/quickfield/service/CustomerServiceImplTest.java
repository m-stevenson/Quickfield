package ca.skyfield.quickfield.service;

import ca.skyfield.quickfield.dto.customer.CustomerResponse;
import ca.skyfield.quickfield.dto.customer.UpdateCustomerRequest;
import ca.skyfield.quickfield.model.Customer;
import ca.skyfield.quickfield.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldGetAllCustomers() {

        List<Customer> customers =
                new ArrayList<>();

        customers.add(Customer.builder()
                .id(1L)
                .firstName("Cust")
                .lastName("Last")
                .phone("555-111-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build()
        );

        customers.add(Customer.builder()
                .id(2L)
                .firstName("Cust two")
                .lastName("Last two")
                .phone("555-111-5555")
                .address("117 Street St")
                .email("email2@test.ca")
                .build()
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Customer> customerPage =
                new PageImpl<>(customers, pageable, 2);

        when(customerRepository.findAll(pageable))
                .thenReturn(customerPage);

        Page<CustomerResponse> result =
                customerService.getAllCustomers(pageable);

        verify(customerRepository)
                .findAll(pageable);

        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).id());
        assertEquals(2L, result.getContent().get(1).id());
        assertEquals("email2@test.ca", result.getContent().get(1).email());
    }

    @Test
    void shouldReturnEmptyPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Customer> customerPage =
                new PageImpl<>(List.of(), pageable, 0);

        when(customerRepository.findAll(pageable))
                .thenReturn(customerPage);

        Page<CustomerResponse> result =
                customerService.getAllCustomers(pageable);

        verify(customerRepository).findAll(pageable);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void shouldGetCustomerById() {

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Cust")
                .lastName("Last")
                .phone("555-111-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build();

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        CustomerResponse result =
            customerService.getCustomerById(1L);

        verify(customerRepository)
                .findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Cust", result.firstName());
        assertEquals("Last", result.lastName());
        assertEquals("email@test.ca", result.email());
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {

        when(customerRepository.findById(55L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> customerService.getCustomerById(5L)
        );

        verify(customerRepository, never())
                .save(any(Customer.class));
    }

    @Test
    void shouldUpdateCustomer() {

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Cust")
                .lastName("Last")
                .phone("555-111-0000")
                .address("123 Street Ave")
                .email("email@test.ca")
                .build();

        UpdateCustomerRequest request =
                new UpdateCustomerRequest(
                        null,
                        "Newman",
                        null,
                        null,
                        null
                );

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(customerRepository.save(Mockito.any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse result =
                customerService.updateCustomer(1L, request);

        verify(customerRepository).save(customer);

        assertEquals(1L, result.id());
        assertEquals("Cust", result.firstName());
        assertEquals("Newman", result.lastName());
        assertEquals("555-111-0000", result.phone());
        assertEquals("123 Street Ave", result.address());
        assertEquals("email@test.ca", result.email());
    }
}
