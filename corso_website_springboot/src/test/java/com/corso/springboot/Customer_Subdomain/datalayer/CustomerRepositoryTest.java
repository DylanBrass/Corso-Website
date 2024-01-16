package com.corso.springboot.Customer_Subdomain.datalayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    Logger log = Logger.getLogger(CustomerRepositoryTest.class.getName());

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void getCustomerByUserId() {
        String userid = "google|123456789";
        Customer customer = customerRepository.getCustomerByUserId(userid);
        assertEquals(userid, customer.getUserId());
    }

    @Test
    void deleteCustomerByUserId() {
        String userid = "google|123456789";
        customerRepository.deleteCustomerByUserId(userid);
        Customer customer = customerRepository.getCustomerByUserId(userid);
        assertNull(customer);
    }

    @Test
    void existsByUserId() {
        String userid = "google|123456789";
        boolean exists = customerRepository.existsByUserId(userid);
        assertTrue(exists);
    }


}