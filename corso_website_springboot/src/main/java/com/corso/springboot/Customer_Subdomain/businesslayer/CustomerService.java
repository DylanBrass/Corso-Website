package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;

public interface CustomerService {
    CustomerResponse getCustomerByUserId(String userId);

    CustomerResponse createCustomer(CustomerRequest customerRequest, String userId);

    CustomerResponse updateCustomer(CustomerRequest customerRequest, String userId);

    void deleteCustomer(String userId);

    boolean checkIfCustomerExists(String userId);

}
