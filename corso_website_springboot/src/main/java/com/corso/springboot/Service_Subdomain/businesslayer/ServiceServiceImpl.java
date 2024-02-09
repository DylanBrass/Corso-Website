package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceIdentifier;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceRequestMapper;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.DatabaseDTOOrdersPerService;
import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.TotalRequestPerDate;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceTimeByResponse;
import com.corso.springboot.cloudinary.CloudinaryService;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import com.corso.springboot.utils.exceptions.services.ServiceBadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceRequestMapper serviceRequestMapper;
    private final ServiceRepository serviceRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ServiceResponse getServiceByServiceId(String serviceId) {
        ServiceEntity service = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
        if (service == null) {
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
                String existingPublicId = cloudinaryService.extractPublicIdFromCloudinaryUrl(existingServiceEntity.getServiceIcon());

                // Update the existing icon image in Cloudinary
                if (!serviceRequest.getServiceIcon().contains("https://") && !serviceRequest.getServiceIcon().contains("http://")) {
                    existingServiceEntity.setServiceIcon(cloudinaryService.updateCloudinaryImage(existingPublicId, serviceRequest.getServiceIcon()));
                }
            }

            // Check if the image needs to be updated
            if (serviceRequest.getServiceImage() != null) {
                // Extract the public ID from the existing Cloudinary URL
                String existingImagePublicId = cloudinaryService.extractPublicIdFromCloudinaryUrl(existingServiceEntity.getServiceImage());

                // Update the existing image in Cloudinary
                if (!serviceRequest.getServiceImage().contains("https://") && !serviceRequest.getServiceImage().contains("http://")) {
                    String updatedImageUrl = cloudinaryService.updateCloudinaryImage(existingImagePublicId, serviceRequest.getServiceImage());
                    existingServiceEntity.setServiceImage(updatedImageUrl);
                }
            }

            // Preparing the response
            return serviceResponseMapper.toServiceResponse(serviceRepository.save(existingServiceEntity));
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

    @Override
    public List<ServiceTimeByResponse> getServiceTimeByService(String dateStart, String dateEnd) {
        try {

            return serviceRepository.getHoursWorkedByServiceFromOrdersInRange(dateStart, dateEnd)
                    .stream()
                    .map(ServiceTimeByResponse::new)
                    .toList();
        } catch (Exception e) {
            throw new ServiceBadRequestException("Failed to generate time by service report :" + e.getMessage());
        }
    }

    @Override
    public List<TotalRequestPerDate> getTotalOrderRequestByService(String dateStart, String dateEnd) {
        if (LocalDate.parse(dateStart).isAfter(LocalDate.parse(dateEnd))) {
            throw new ServiceBadRequestException("Invalid date range");
        }
        try {
            List<DatabaseDTOOrdersPerService> databaseDTOOrdersPerServices = serviceRepository.getTotalOrderRequestByService(dateStart, dateEnd)
                    .stream()
                    .map(DatabaseDTOOrdersPerService::new)
                    .toList();

            List<ServiceResponse> services = getAllServices();

            Map<LocalDate, List<DatabaseDTOOrdersPerService>> groupedByDate = databaseDTOOrdersPerServices.stream()
                    .collect(Collectors.groupingBy(DatabaseDTOOrdersPerService::getDate));

            // Add missing services with 0 total order request on that day
            for (Map.Entry<LocalDate, List<DatabaseDTOOrdersPerService>> entry : groupedByDate.entrySet()) {
                List<DatabaseDTOOrdersPerService> missingServices = services.stream()
                        .filter(service -> entry.getValue().stream().noneMatch(dto -> dto.getServiceId().equals(service.getServiceId())))
                        .map(service -> new DatabaseDTOOrdersPerService(service.getServiceId(), service.getServiceName(), 0L, entry.getKey()))
                        .toList();

                entry.getValue().addAll(missingServices);
            }

            return groupedByDate.entrySet().stream()
                    .map(entry -> new TotalRequestPerDate(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(TotalRequestPerDate::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceBadRequestException("Failed to generate total order request by service report :" + e.getMessage());
        }
    }
}
