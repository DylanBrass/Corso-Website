package com.corso.springboot.Customer_Subdomain.businesslayer;

import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    CustomerResponse getCustomerByUserId(String userId);

    CustomerResponse createCustomer(CustomerRequest customerRequest, String userId);

    CustomerResponse updateCustomer(CustomerRequest customerRequest, String userId);

    void deleteCustomer(String userId);

    boolean checkIfCustomerExists(String userId);

    List<CustomerResponse> getCustomerByQueryParams(Map<String, String> requestParams);

}
