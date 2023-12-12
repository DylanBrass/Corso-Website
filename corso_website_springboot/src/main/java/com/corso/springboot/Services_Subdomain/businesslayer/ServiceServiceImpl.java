package com.corso.springboot.Services_Subdomain.businesslayer;

import com.corso.springboot.Services_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Services_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Services_Subdomain.presentationlayer.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService{

    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceResponseMapper.toServicesResponse(serviceRepository.findAll());
    }
}
