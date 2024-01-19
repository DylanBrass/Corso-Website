package com.corso.springboot.Service_Subdomain.presentationlayer;

import com.corso.springboot.Service_Subdomain.businesslayer.CloudinaryService;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceEntity;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc//(addFilters = false)
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

    ServiceEntity service = new ServiceEntity("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image");

    @BeforeEach
    public void setUp() {
        serviceRepository.deleteAll();
        serviceRepository.save(service);
    }
    @Test
    public void whenServiceByIdExists_thenReturnService() {
        //arrange
        String serviceId = service.getServiceIdentifier().getServiceId();

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
        Integer expectedNumServices = 1;
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
    public void whenGettingCountOfAllServices_thenReturnCount() throws Exception {
        //act
        mockMvc.perform(get(BASE_URI_SERVICES + "/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
