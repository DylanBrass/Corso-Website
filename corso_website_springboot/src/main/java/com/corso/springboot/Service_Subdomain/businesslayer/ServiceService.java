package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;

import java.util.List;

public interface ServiceService {
    ServiceResponse getServiceByServiceId(String serviceId);

    List<ServiceResponse> getAllServices();

    ServiceResponse createService(ServiceRequest serviceRequest);
}
