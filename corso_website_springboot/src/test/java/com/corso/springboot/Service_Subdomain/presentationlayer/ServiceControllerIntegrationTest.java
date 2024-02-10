package com.corso.springboot.Service_Subdomain.presentationlayer;

import com.corso.springboot.cloudinary.CloudinaryService;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ServiceControllerIntegrationTest {

    private final String BASE_URI_SERVICES = "/api/v1/corso/services";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ServiceRepository serviceRepository;

    @MockBean
    private CloudinaryService cloudinaryService;

    @Test
    public void whenServiceByIdExists_thenReturnService() {
        //arrange
        String serviceId = "982577e6-1909-46b8-8583-e08c9daa4e9b";

        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/" + serviceId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.serviceId").isEqualTo(serviceId);
    }

    @Test
    public void whenGetAllServices_thenReturnAllServices() {
        Integer expectedNumServices = 5;
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumServices);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenCreatingServiceWithValidValues_thenReturnCreatedService() throws Exception {
        // Mock the CloudinaryService behavior
        Mockito.when(cloudinaryService.uploadBase64Image(Mockito.anyString())).thenReturn("mockedImageUrl");

        // Prepare a valid ServiceRequest
        String serviceRequestJson = "{ \"serviceName\": \"Test Service\", \"serviceDescription\": \"Test Description\", \"serviceIcon\": \"base64Icon\", \"serviceImage\": \"base64Image\" }";

        // Perform the POST request to create a service
        mockMvc.perform(post(BASE_URI_SERVICES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceRequestJson)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceName").value("Test Service"))
                .andExpect(jsonPath("$.serviceDescription").value("Test Description"))
                .andExpect(jsonPath("$.serviceIcon").value("mockedImageUrl"))
                .andExpect(jsonPath("$.serviceImage").value("mockedImageUrl"));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenModifyingServiceWithValidValues_thenReturnModifiedService() throws Exception {
        // Arrange
        String serviceId = "982577e6-1909-46b8-8583-e08c9daa4e9b";

        // Mock the CloudinaryService behavior
        Mockito.when(cloudinaryService.extractPublicIdFromCloudinaryUrl(Mockito.anyString())).thenReturn("public-id");
        Mockito.when(cloudinaryService.updateCloudinaryImage(Mockito.anyString(), Mockito.anyString())).thenReturn("updatedImageUrl");

        // Prepare a valid ServiceRequest for modification
        String serviceRequestJson = "{ \"serviceName\": \"Modified Service\", \"serviceDescription\": \"Modified Description\", \"serviceIcon\": \"base64ModifiedIcon\", \"serviceImage\": \"base64ModifiedImage\" }";

        // Perform the PUT request to modify the service
        mockMvc.perform(put(BASE_URI_SERVICES + "/" + serviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceRequestJson)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value(serviceId))
                .andExpect(jsonPath("$.serviceName").value("Modified Service"))
                .andExpect(jsonPath("$.serviceDescription").value("Modified Description"))
                .andExpect(jsonPath("$.serviceIcon").value("updatedImageUrl"))
                .andExpect(jsonPath("$.serviceImage").value("updatedImageUrl"));

        // Verify that the service in the repository has been updated
        ServiceEntity modifiedService = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
        assertNotNull(modifiedService);
        assertEquals("Modified Service", modifiedService.getServiceName());
        assertEquals("Modified Description", modifiedService.getServiceDescription());
        assertEquals("updatedImageUrl", modifiedService.getServiceIcon());
        assertEquals("updatedImageUrl", modifiedService.getServiceImage());
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenGettingCountOfAllServices_thenReturnCount() throws Exception {
        //act
        mockMvc.perform(get(BASE_URI_SERVICES + "/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenGettingAllVisibleServices_thenReturnAllVisibleServices() throws Exception {

        //act
        mockMvc.perform(get(BASE_URI_SERVICES + "/visible")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenChangingServiceVisibility_thenReturnModifiedService() throws Exception {
        // Arrange
        String serviceId = "982577e6-1909-46b8-8583-e08c9daa4e9b";

        //act
        mockMvc.perform(patch(BASE_URI_SERVICES + "/" + serviceId + "/visibility")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value(serviceId))
                .andExpect(jsonPath("$.active").value(false));

        // Verify that the service in the repository has been updated
        ServiceEntity modifiedService = serviceRepository.findByServiceIdentifier_ServiceId(serviceId);
        assertNotNull(modifiedService);
        assertFalse(modifiedService.isActive());
    }


    @Test
    @WithMockUser(authorities = "Admin")
    void getTimeByServiceReport_ShouldSucceed() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/time-by-service?date_start=2021-01-01&date_end=2025-12-31")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0].serviceId").isEqualTo("982577e6-1909-46b8-8583-e08c9daa4e9b")
                .jsonPath("$[0].serviceName").isEqualTo("Electrical Repairs")
                .jsonPath("$[0].hours_worked").isEqualTo(6)
                .jsonPath("$[1].serviceId").isEqualTo("b581b538-d6d5-44f1-b288-b45824e4dd4c")
                .jsonPath("$[1].serviceName").isEqualTo("Lighting Installation")
                .jsonPath("$[1].hours_worked").isEqualTo(0);

    }


    @Test
    @WithMockUser(authorities = "Admin")
    void getOrderPerServices_ShouldSucceed() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/orders-per-service?date_start=2021-01-01&date_end=2025-12-31")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].length()").isEqualTo(6)
                .jsonPath("$.length()").isEqualTo(60);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getOrdersPerServiceWithLessThenAYearRange_ShouldSucceed() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/orders-per-service?date_start=2023-03-25&date_end=2024-03-22")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].length()").isEqualTo(6)
                .jsonPath("$.length()").isEqualTo(13);

    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getOrdersPerServiceWithLessThenTwoMonths_ShouldSucceed() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/orders-per-service?date_start=2024-01-25&date_end=2024-03-22")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].length()").isEqualTo(6)
                .jsonPath("$.length()").isEqualTo(58);

    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getTotalOrdersPerService_ShouldSucceed() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/total-orders-per-service?date_start=2021-01-01&date_end=2025-12-31")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].length()").isEqualTo(3)
                .jsonPath("$[0].totalOrderRequest").isEqualTo(1)
                .jsonPath("$.length()").isEqualTo(5);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getTotalOrdersPerServiceWithInvalidDateRange_ShouldFail() {
        //act
        webTestClient.get()
                .uri(BASE_URI_SERVICES + "/total-orders-per-service?date_start=2026-01-01&date_end=2025-12-31")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isBadRequest();
    }


}
