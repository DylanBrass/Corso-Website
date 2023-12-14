package com.corso.springboot.Customer_Subdomain.datamapperlayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {


    CustomerResponse toCustomerResponse(Customer customer);
}
