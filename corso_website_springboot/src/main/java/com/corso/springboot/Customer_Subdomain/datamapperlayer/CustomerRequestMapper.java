package com.corso.springboot.Customer_Subdomain.datamapperlayer;

import com.corso.springboot.Customer_Subdomain.datalayer.Customer;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {

    @Mapping(ignore = true, target = "userId")
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "verified")
    Customer toCustomer(CustomerRequest customerRequest);
}
