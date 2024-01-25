package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceRequestMapper;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.cloudinary.CloudinaryService;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        ServiceEntity service = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
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
    public List<ServiceResponse> getAllVisibleServices() {
        return serviceResponseMapper.toServicesResponse(serviceRepository.findAllByIsActive(true));
    }

    @Override
    public ServiceResponse createService(ServiceRequest serviceRequest) {
        try {
            String iconUrl = cloudinaryService.uploadBase64Image(serviceRequest.getServiceIcon());
            String imageUrl = cloudinaryService.uploadBase64Image(serviceRequest.getServiceImage());

            // Mapping request to entity and setting additional properties
            ServiceEntity service = serviceRequestMapper.toService(serviceRequest);
            service.setServiceIdentifier(new ServiceIdentifier());
            service.setServiceIcon(iconUrl);
            service.setServiceImage(imageUrl);

            service.setActive(true);



            // Saving the service
            ServiceEntity savedService = serviceRepository.save(service);

            // Preparing the response
            return serviceResponseMapper.toServiceResponse(savedService);
            

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload images", e);
        }
    }

    @Override
    public ServiceResponse modifyService(String serviceId, ServiceRequest serviceRequest) {
        try {
            // Find the existing service
            ServiceEntity existingServiceEntity = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);

            if (existingServiceEntity == null) {
                throw new ServiceNotFoundException("No service found.");
            }

            // Update properties from the request
            existingServiceEntity.setServiceName(serviceRequest.getServiceName());
            existingServiceEntity.setServiceDescription(serviceRequest.getServiceDescription());

            // Check if the icon needs to be updated
            if (serviceRequest.getServiceIcon() != null) {
                // Extract the public ID from the existing Cloudinary URL
                String existingIconUrl = existingServiceEntity.getServiceIcon();
                String existingPublicId = cloudinaryService.extractPublicIdFromCloudinaryUrl(existingIconUrl);

                // Update the existing icon image in Cloudinary
                if(!serviceRequest.getServiceIcon().contains("https://") && !serviceRequest.getServiceIcon().contains("http://")) {
                    String updatedIconUrl = cloudinaryService.updateCloudinaryImage(existingPublicId, serviceRequest.getServiceIcon());
                    existingServiceEntity.setServiceIcon(updatedIconUrl);
                }
            }

            // Check if the image needs to be updated
            if (serviceRequest.getServiceImage() != null) {
                // Extract the public ID from the existing Cloudinary URL
                String existingImageUrl = existingServiceEntity.getServiceImage();
                String existingImagePublicId = cloudinaryService.extractPublicIdFromCloudinaryUrl(existingImageUrl);

                // Update the existing image in Cloudinary
                if(!serviceRequest.getServiceImage().contains("https://") && !serviceRequest.getServiceImage().contains("http://")) {
                    String updatedImageUrl = cloudinaryService.updateCloudinaryImage(existingImagePublicId, serviceRequest.getServiceImage());
                    existingServiceEntity.setServiceImage(updatedImageUrl);
                }
            }

            // Save the updated service
            serviceRepository.save(existingServiceEntity);

            // Preparing the response
            return serviceResponseMapper.toServiceResponse(existingServiceEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update images.", e);
        }
    }

    @Override
    public ServiceResponse changeServiceVisibility(String serviceId) {
        ServiceEntity existingServiceEntity = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);

        if (existingServiceEntity == null) {
            throw new ServiceNotFoundException("No service found.");
        }

        existingServiceEntity.setActive(!existingServiceEntity.isActive());
        serviceRepository.save(existingServiceEntity);

        return serviceResponseMapper.toServiceResponse(existingServiceEntity);
    }


    @Override
    public int countAllServices() {
        return serviceRepository.countAllBy();
    }
}
