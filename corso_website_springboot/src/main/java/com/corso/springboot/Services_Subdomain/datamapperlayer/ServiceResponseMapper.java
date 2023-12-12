package com.corso.springboot.Services_Subdomain.datamapperlayer;

import com.corso.springboot.Services_Subdomain.datalayer.Service;
import com.corso.springboot.Services_Subdomain.presentationlayer.ServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceResponseMapper {

    @Mapping(expression = "java(service.getServiceId().getServiceId())", target = "serviceId")
    ServiceResponse toServiceResponse(Service service);
    List<ServiceResponse> toServicesResponse(List<Service> services);
}
