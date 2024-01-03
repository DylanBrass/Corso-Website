package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceRequestMapper;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService{

    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRequestMapper serviceRequestMapper;
    private final ServiceRepository serviceRepository;
    private final CloudinaryService cloudinaryService;

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

    @Override
    public ServiceResponse createService(ServiceRequest serviceRequest) {
        try {
            String iconUrl = cloudinaryService.uploadBase64Image(serviceRequest.getServiceIcon());
            String imageUrl = cloudinaryService.uploadBase64Image(serviceRequest.getServiceImage());

            // Mapping request to entity and setting additional properties
            com.corso.springboot.Service_Subdomain.datalayer.Service service = serviceRequestMapper.toService(serviceRequest);
            service.setServiceIdentifier(new ServiceIdentifier());
            service.setServiceIcon(iconUrl);
            service.setServiceImage(imageUrl);

            // Saving the service
            com.corso.springboot.Service_Subdomain.datalayer.Service savedService = serviceRepository.save(service);

            // Preparing the response
            return serviceResponseMapper.toServiceResponse(savedService);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload images", e);
        }
    }
}
