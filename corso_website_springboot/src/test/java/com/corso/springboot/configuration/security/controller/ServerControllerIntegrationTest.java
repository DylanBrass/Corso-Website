package com.corso.springboot.configuration.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ServerControllerIntegrationTest {
    private final String BASE_URI_SERVER = "/api/v1/corso/server";

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void whenConnectionVerification_thenReturnFound() throws Exception {
        //arrange
        //act
        webTestClient.head()
                .uri(BASE_URI_SERVER + "/status")
                .exchange().expectStatus().isFound();
    }
}