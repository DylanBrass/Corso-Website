package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
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
        return serviceResponseMapper.toServiceResponse(serviceRepository.findByServiceIdentifier_ServiceId(serviceId));
    }

}
