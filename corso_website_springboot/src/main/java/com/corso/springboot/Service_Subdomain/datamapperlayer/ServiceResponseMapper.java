package com.corso.springboot.Service_Subdomain.datamapperlayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceResponseMapper {

    @Mapping(expression = "java(service.getServiceIdentifier().getServiceId())", target = "serviceId")
    @Mapping(expression = "java(service.isActive())", target = "isActive")
    ServiceResponse toServiceResponse(ServiceEntity service);
    List<ServiceResponse> toServicesResponse(List<ServiceEntity> services);
}