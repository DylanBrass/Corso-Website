package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService{

    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRepository serviceRepository;

    @Override
    public ServiceResponse getServiceByServiceId(String serviceId) {
        com.corso.springboot.Service_Subdomain.datalayer.Service service = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
        if(service == null){
            throw new ServiceNotFoundException("Unknown service id : " + serviceId);
        }
        return serviceResponseMapper.toServiceResponse(service);
    }


    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceResponseMapper.toServicesResponse(serviceRepository.findAll());
    }
}
