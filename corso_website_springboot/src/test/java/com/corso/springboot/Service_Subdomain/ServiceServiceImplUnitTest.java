package com.corso.springboot.Service_Subdomain;

import com.corso.springboot.Service_Subdomain.businesslayer.ServiceServiceImpl;
import com.corso.springboot.Service_Subdomain.datalayer.Service;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import com.corso.springboot.Service_Subdomain.datamapperlayer.ServiceResponseMapper;
import com.corso.springboot.Service_Subdomain.presentationlayer.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ServiceServiceImplUnitTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceResponseMapper serviceResponseMapper;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Test
    void getServiceByServiceId_ShouldSucceed() {
        // Arrange
        ServiceResponse mockServiceResponse = new ServiceResponse("1", "Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image");

        Mockito.when(serviceRepository.findByServiceIdentifier_ServiceId(Mockito.anyString())).thenReturn(new Service("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image"));
        Mockito.when(serviceResponseMapper.toServiceResponse(Mockito.any())).thenReturn(mockServiceResponse);

        // Act
        ServiceResponse result = serviceService.getServiceByServiceId("1");

        // Assert
        assertEquals(mockServiceResponse, result);
        Mockito.verify(serviceRepository, Mockito.times(1)).findByServiceIdentifier_ServiceId(Mockito.anyString());
        Mockito.verify(serviceResponseMapper, Mockito.times(1)).toServiceResponse(Mockito.any());

    }
}