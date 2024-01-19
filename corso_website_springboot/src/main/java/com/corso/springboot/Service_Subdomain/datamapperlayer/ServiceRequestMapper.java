package com.corso.springboot.Service_Subdomain.datamapperlayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceRequestMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "serviceIdentifier")
    ServiceEntity toService(ServiceRequest serviceRequest);
}
