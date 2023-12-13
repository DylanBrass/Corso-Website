package com.corso.springboot.Service_Subdomain;

import com.corso.springboot.Service_Subdomain.datalayer.Service;
import com.corso.springboot.Service_Subdomain.datalayer.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ServiceControllerIntegrationTest {

    private final String BASE_URI_SERVICES = "/api/v1/corso/services";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ServiceRepository serviceRepository;

    Service service = new Service("Test Service 1", "Test Service 1 Description", "Test Service 1 Icon", "Test Service 1 Image");

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
}
