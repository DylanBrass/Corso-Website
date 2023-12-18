package com.corso.springboot.Customer_Subdomain.datamapperlayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {


    CustomerResponse toCustomerResponse(Customer customer);
    List<CustomerResponse> toCustomersResponse(List<Customer> allByNameAndEmail);
}
