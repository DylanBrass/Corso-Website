package com.corso.springboot.Service_Subdomain.businesslayer;

import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceRequestMapper;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.DatabaseDTOOrdersPerService;
import com.corso.springboot.Service_Subdomain.presentationlayer.OrdersPerService.DatabaseDTOTotalOrdersPerService;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceRequest;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceTimeByResponse;
import com.corso.springboot.cloudinary.CloudinaryService;
import com.corso.springboot.utils.exceptions.ServiceNotFoundException;
import com.corso.springboot.utils.exceptions.services.ServiceBadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceServiceImplUnitTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceResponseMapper serviceResponseMapper;

    @Mock
    private ServiceRequestMapper serviceRequestMapper;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @BeforeEach
    void setUp() {
        serviceService = new ServiceServiceImpl(serviceResponseMapper, serviceRequestMapper, serviceRepository, cloudinaryService);
    }

    @Test
    void getServiceByServiceId_ShouldSucceed() {
        // Arrange
        ServiceResponse mockServiceResponse = new ServiceResponse("1", "Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true);

        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(new ServiceEntity("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true));
        when(serviceResponseMapper.toServiceResponse(Mockito.any())).thenReturn(mockServiceResponse);

        // Act
        ServiceResponse result = serviceService.getServiceByServiceId("1");

        // Assert
        assertEquals(mockServiceResponse, result);
        Mockito.verify(serviceRepository, Mockito.times(1)).findByServiceIdentifier_ServiceId(Mockito.anyString());
        Mockito.verify(serviceResponseMapper, Mockito.times(1)).toServiceResponse(Mockito.any());

    }

    @Test
    void getServiceByServiceId_ShouldThrowException() {
        // Arrange
        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(null);

        // Act & Assert
        try {
            serviceService.getServiceByServiceId("1");
        } catch (Exception e) {
            assertEquals("Unknown service id : 1", e.getMessage());
        }
        Mockito.verify(serviceRepository, Mockito.times(1)).findByServiceIdentifier_ServiceId(Mockito.anyString());
    }

    @Test
    void getAllServices_ShouldSucceed() {
        // Arrange
        List<ServiceResponse> mockServiceResponses = Arrays.asList(
                new ServiceResponse("1", "Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true),
                new ServiceResponse("2", "Test Service 2", "Test Service 2 Description", "Test Service 2 Icon", "Test Service 2 Image", true)
        );

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(
                new ServiceEntity("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true),
                new ServiceEntity("Test Service 2", "Test Service 2 Description", "Test Service 2 Icon", "Test Service 2 Image", true)));

        when(serviceResponseMapper.toServicesResponse(Mockito.anyList())).thenReturn(mockServiceResponses);

        // Act
        List<ServiceResponse> result = serviceService.getAllServices();

        // Assert
        assertEquals(mockServiceResponses, result);
        Mockito.verify(serviceRepository, Mockito.times(1)).findAll();
        Mockito.verify(serviceResponseMapper, Mockito.times(1)).toServicesResponse(Mockito.anyList());
    }

    @Test
    void getAllVisibleServices_ShouldSucceed() {
        // Arrange
        List<ServiceResponse> mockServiceResponses = Arrays.asList(
                new ServiceResponse("1", "Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true),
                new ServiceResponse("2", "Test Service 2", "Test Service 2 Description", "Test Service 2 Icon", "Test Service 2 Image", true)
        );

        when(serviceRepository.findAllByIsActive(true)).thenReturn(Arrays.asList(
                new ServiceEntity("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true),
                new ServiceEntity("Test Service 2", "Test Service 2 Description", "Test Service 2 Icon", "Test Service 2 Image", true)));

        when(serviceResponseMapper.toServicesResponse(Mockito.anyList())).thenReturn(mockServiceResponses);

        // Act
        List<ServiceResponse> result = serviceService.getAllVisibleServices();

        // Assert
        assertEquals(mockServiceResponses, result);
        Mockito.verify(serviceRepository, Mockito.times(1)).findAllByIsActive(true);
        Mockito.verify(serviceResponseMapper, Mockito.times(1)).toServicesResponse(Mockito.anyList());
        assertEquals(2, result.size());
        assertEquals("Test Service 1", result.get(0).getServiceName());
        assertEquals("Test Service 2", result.get(1).getServiceName());
        assertTrue(result.get(0).isActive());
        assertTrue(result.get(1).isActive());
    }

    @Test
    void createService_ShouldSucceed() throws IOException {
        // Arrange
        ServiceRequest mockServiceRequest = ServiceRequest.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("thunderBolt_yguofq.png")
                .serviceImage("test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceEntity mockService = ServiceEntity.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceResponse mockServiceResponse = ServiceResponse.builder()
                .serviceId("1")
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();

        when(serviceRequestMapper.toService(mockServiceRequest)).thenReturn(mockService);
        when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenReturn("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png");
        when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenReturn("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg");
        when(serviceRepository.save(mockService)).thenReturn(mockService);
        when(serviceResponseMapper.toServiceResponse(mockService)).thenReturn(mockServiceResponse);

        // Act
        ServiceResponse result = serviceService.createService(mockServiceRequest);

        // Assert
        assertEquals(mockServiceResponse, result);
        Mockito.verify(serviceRequestMapper, Mockito.times(1)).toService(Mockito.any());
        Mockito.verify(cloudinaryService, Mockito.times(2)).uploadBase64Image(Mockito.anyString());
        Mockito.verify(serviceRepository, Mockito.times(1)).save(Mockito.any());
        assertEquals(mockServiceResponse.getServiceId(), result.getServiceId());
        assertEquals(mockServiceResponse.getServiceName(), result.getServiceName());
        assertEquals(mockServiceResponse.getServiceDescription(), result.getServiceDescription());
        assertEquals(mockServiceResponse.getServiceIcon(), result.getServiceIcon());
        assertEquals(mockServiceResponse.getServiceImage(), result.getServiceImage());
    }

    @Test
    void createServiceWithIOException_ShouldThrowRuntimeExceptionOnIOException() throws IOException {
        // Arrange
        ServiceRequest mockServiceRequest = ServiceRequest.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("thunderBolt_yguofq.png")
                .serviceImage("test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceEntity mockService = ServiceEntity.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();


        when(serviceRequestMapper.toService(mockServiceRequest)).thenReturn(mockService);
        when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenThrow(new IOException("Simulated IOException"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceService.createService(mockServiceRequest);
        });

        assertEquals("Failed to upload images", exception.getMessage());
        assertEquals("Simulated IOException", exception.getCause().getMessage());

        Mockito.verify(serviceRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void countAllServices_ShouldSucceed() {
        // Arrange
        when(serviceRepository.countAllBy()).thenReturn(2);

        // Act
        int result = serviceService.countAllServices();

        // Assert
        assertEquals(2, result);
        Mockito.verify(serviceRepository, Mockito.times(1)).countAllBy();
    }

    @Test
    void modifyService_ShouldSucceed() throws IOException {
        // Arrange
        ServiceRequest mockServiceRequest = ServiceRequest.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("thunderBolt_yguofq.png")
                .serviceImage("test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceEntity mockService = ServiceEntity.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceResponse mockServiceResponse = ServiceResponse.builder()
                .serviceId("1")
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();

        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(mockService);
        when(serviceRequestMapper.toService(mockServiceRequest)).thenReturn(mockService);
        when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenReturn("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png");
        when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenReturn("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg");
        when(serviceRepository.save(mockService)).thenReturn(mockService);
        when(serviceResponseMapper.toServiceResponse(mockService)).thenReturn(mockServiceResponse);

        // Act
        ServiceResponse result = serviceService.modifyService("1", mockServiceRequest);

        // Assert
        assertEquals(mockServiceResponse, result);
        assertEquals(mockServiceResponse.getServiceId(), result.getServiceId());
        assertEquals(mockServiceResponse.getServiceName(), result.getServiceName());
        assertEquals(mockServiceResponse.getServiceDescription(), result.getServiceDescription());
        assertEquals(mockServiceResponse.getServiceIcon(), result.getServiceIcon());
        assertEquals(mockServiceResponse.getServiceImage(), result.getServiceImage());
    }

    @Test
    void modifyService_ShouldThrowRuntimeExceptionOnIOException() throws IOException {
        // Arrange
        ServiceRequest mockServiceRequest = ServiceRequest.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("thunderBolt_yguofq.png")
                .serviceImage("test-image-carousel-5_jhydhp.jpg")
                .build();

        ServiceEntity mockService = ServiceEntity.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .build();

        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(mockService);
        when(serviceRequestMapper.toService(mockServiceRequest)).thenReturn(mockService);
        when(cloudinaryService.extractPublicIdFromCloudinaryUrl(Mockito.anyString())).thenReturn("public_id");
        when(cloudinaryService.updateCloudinaryImage(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException("Simulated IOException"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceService.modifyService("1", mockServiceRequest);
        });

        assertEquals("Failed to update images.", exception.getMessage());
        assertEquals("Simulated IOException", exception.getCause().getMessage());
    }


    @Test
    void whenModifyServiceNotFound_thenReturnException() throws IOException {
        // Arrange
        ServiceRequest mockServiceRequest = ServiceRequest.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("thunderBolt_yguofq.png")
                .serviceImage("test-image-carousel-5_jhydhp.jpg")
                .build();

        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(null);

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            serviceService.modifyService("1", mockServiceRequest);
        });

        assertEquals("No service found.", exception.getMessage());
    }

    @Test
    void changeServiceVisibility_ShouldSucceed() {
        // Arrange
        ServiceEntity mockService = ServiceEntity.builder()
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .isActive(true)
                .build();

        ServiceResponse mockServiceResponse = ServiceResponse.builder()
                .serviceId("1")
                .serviceName("Test Service 1")
                .serviceDescription("Test Service 1 Description")
                .serviceIcon("https://res.cloudinary.com/dszhbawv7/image/upload/v1701973966/ThunderBolt_yguofq.png")
                .serviceImage("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .isActive(false)
                .build();

        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(mockService);
        when(serviceRepository.save(mockService)).thenReturn(mockService);
        when(serviceResponseMapper.toServiceResponse(mockService)).thenReturn(mockServiceResponse);

        // Act
        ServiceResponse result = serviceService.changeServiceVisibility("1");

        // Assert
        assertEquals(mockServiceResponse, result);
        assertEquals(mockServiceResponse.getServiceId(), result.getServiceId());
        assertEquals(mockServiceResponse.getServiceName(), result.getServiceName());
        assertEquals(mockServiceResponse.getServiceDescription(), result.getServiceDescription());
        assertEquals(mockServiceResponse.getServiceIcon(), result.getServiceIcon());
        assertEquals(mockServiceResponse.getServiceImage(), result.getServiceImage());
        assertEquals(mockServiceResponse.isActive(), result.isActive());
    }

    @Test
    void whenChangeServiceVisibilityNotFound_thenReturnException() {
        // Arrange
        when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(null);

        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () -> {
            serviceService.changeServiceVisibility("1");
        });

        assertEquals("No service found.", exception.getMessage());
    }


    @Test
    void whenGettingServiceTimeBy_DatabaseShouldThrowError(){

        // Arrange
        when(serviceRepository.getHoursWorkedByServiceFromOrdersInRange(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException("Simulated Database Error"));

        // Act & Assert
        ServiceBadRequestException exception = assertThrows(ServiceBadRequestException.class, () -> {
            serviceService.getServiceTimeByService("2021-01-01", "2021-12-31");
        });

        assertEquals("Failed to generate time by service report :Simulated Database Error", exception.getMessage());

    }

    @Test
    void whenCallingServiceByTimeResponseWithBadlyFormedTuple_thenReturnException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image", true};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceTimeByResponse(tuple);
        });

        assertEquals("Invalid tuple length: 6", exception.getMessage());
    }

    @Test
    void whenCallingServiceByTimeResponseWithBadTypeOnTuplePositionOne_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{1, "Test Service 1", 10L};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceTimeByResponse(tuple);
        });

        assertEquals("Invalid type for first field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingServiceByTimeResponseWithBadTypeOnTuplePositionTwo_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", 1, 10L};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceTimeByResponse(tuple);
        });

        assertEquals("Invalid type for second field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingServiceByTimeResponseWithBadTypeOnTuplePositionThree_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", 10};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServiceTimeByResponse(tuple);
        });

        assertEquals("Invalid type for third field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenGettingTotalOrdersPerServiceWithDatabaseError_thenReturnException() {
        // Arrange
        when(serviceRepository.getTotalOrderRequestByService(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException("Simulated Database Error"));

        // Act & Assert
        ServiceBadRequestException exception = assertThrows(ServiceBadRequestException.class, () -> {
            serviceService.getTotalOrderRequestByService("2021-01-01", "2021-12-31");
        });

        assertEquals("Failed to generate total order request by service report :Simulated Database Error", exception.getMessage());
    }


    @Test
    void whenCallingDatabaseDTOOrdersPerServiceWithBadlyFormedTuple_thenReturnException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", 10L};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOOrdersPerService(tuple);
        });

        assertEquals("Invalid tuple length: 3", exception.getMessage());
    }


    @Test
    void whenCallingDatabaseDTOOrdersPerServiceWithBadTypeOnTuplePositionOne_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{1, "Test Service 1", 10L, "2021-01-01"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOOrdersPerService(tuple);
        });

        assertEquals("Invalid type for first field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingDatabaseDTOOrdersPerServiceWithBadTypeOnTuplePositionTwo_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", 1, 10L, "2021-01-01"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOOrdersPerService(tuple);
        });

        assertEquals("Invalid type for second field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingDatabaseDTOOrdersPerServiceWithBadTypeOnTuplePositionThree_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", "10", "2021-01-01"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOOrdersPerService(tuple);
        });

        assertEquals("Invalid type for third field of tuple: class java.lang.String", exception.getMessage());
    }

    @Test
    void whenCallingDatabaseDTOOrdersPerServiceWithBadTypeOnTuplePositionFour_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", 10L, 1};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOOrdersPerService(tuple);
        });

        assertEquals("Invalid type for fourth field of tuple: class java.lang.Integer", exception.getMessage());
    }


    @Test
    void whenCallingDatabaseDTOTotalOrdersPerServiceWithBadlyFormedTuple_thenReturnException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", 10L, "2021-01-01"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOTotalOrdersPerService(tuple);
        });

        assertEquals("Invalid tuple length: 4", exception.getMessage());
    }


    @Test
    void whenCallingDatabaseDTOTotalOrdersPerServiceWithBadTypeOnTuplePositionOne_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{1, "Test Service 1", 10L};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOTotalOrdersPerService(tuple);
        });

        assertEquals("Invalid type for first field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingDatabaseDTOTotalOrdersPerServiceWithBadTypeOnTuplePositionTwo_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", 1, 10L};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOTotalOrdersPerService(tuple);
        });

        assertEquals("Invalid type for second field of tuple: class java.lang.Integer", exception.getMessage());
    }

    @Test
    void whenCallingDatabaseDTOTotalOrdersPerServiceWithBadTypeOnTuplePositionThree_ShouldThrowException() {
        // Arrange
        Object[] tuple = new Object[]{"1", "Test Service 1", "10"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseDTOTotalOrdersPerService(tuple);
        });

        assertEquals("Invalid type for third field of tuple: class java.lang.String", exception.getMessage());
    }




}