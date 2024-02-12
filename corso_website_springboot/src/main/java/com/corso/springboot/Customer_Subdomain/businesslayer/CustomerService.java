package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

public interface CustomerService {
    CustomerResponse getCustomerByUserId(String userId);

    CustomerResponse createCustomer(CustomerRequest customerRequest, String userId, String email) throws MessagingException;

    CustomerResponse updateCustomer(CustomerRequest customerRequest, String userId, String email) throws MessagingException;

    void deleteCustomer(String userId);

    boolean checkIfCustomerExists(String userId);

    List<CustomerResponse> getCustomerByQueryParams(Map<String, String> requestParams);

    void verifyEmail(String token,String email);

    void verifyCustomer(String userId) throws MessagingException;
}
