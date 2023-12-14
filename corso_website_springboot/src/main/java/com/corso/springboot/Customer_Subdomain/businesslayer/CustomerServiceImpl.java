package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.datalayer.CustomerRepository;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerRequestMapper;
import com.corso.springboot.Customer_Subdomain.datamapperlayer.CustomerResponseMapper;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import com.corso.springboot.utils.exceptions.CustomerNotFoundException;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;

    private final CustomerResponseMapper customerResponseMapper;

    private final CustomerRequestMapper customerRequestMapper;


    @Override
    public CustomerResponse getCustomerByUserId(String userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");


        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest, String userId) {

        if (customerRepository.existsByUserId(userId))
            throw new InvalidRequestException("Customer already exists");

        log.info("Creating customer for user {}", userId);
        log.info("Customer request {}", customerRequest);

        Customer customer = customerRequestMapper.toCustomer(customerRequest);

        customer.setUserId(userId);
        customerRepository.save(customer);


        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest, String userId) {
        Customer customer = customerRepository.getCustomerByUserId(userId);

        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");

        log.info("Updating customer for user {}", userId);
        log.info("Customer request {}", customerRequest);

        customer.setName(customerRequest.getName() != null ? customerRequest.getName() : customer.getName());
        customer.setEmail(customerRequest.getEmail() != null ? customerRequest.getEmail() : customer.getEmail());
        customer.setPhone(customerRequest.getPhone() != null ? customerRequest.getPhone() : customer.getPhone());
        customer.setAddress(customerRequest.getAddress() != null ? customerRequest.getAddress() : customer.getAddress());
        customer.setPostalCode(customerRequest.getPostalCode() != null ? customerRequest.getPostalCode() : customer.getPostalCode());
        customer.setCity(customerRequest.getCity() != null ? customerRequest.getCity() : customer.getCity());
        customer.setApartmentNumber(customerRequest.getApartmentNumber() != null ? customerRequest.getApartmentNumber() : customer.getApartmentNumber());



        customerRepository.save(customer);

        return customerResponseMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(String userId) {

        if (!customerRepository.existsByUserId(userId))
            throw new CustomerNotFoundException("Customer not found");

        customerRepository.deleteCustomerByUserId(userId);

        if (customerRepository.existsByUserId(userId))
            throw new InvalidRequestException("Customer not deleted");

    }

    @Override
    public boolean checkIfCustomerExists(String userId) {
        return customerRepository.existsByUserId(userId);
    }
}
