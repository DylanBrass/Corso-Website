package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.datalayer.OrderRepository;
import com.corso.springboot.Order_Subdomain.datalayer.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class OrderControllerIntegrationTest {
    private final String BASE_URI_ORDERS = "/api/v1/corso/orders";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        orderRepository.save(new Order("auth0|65702e81e9661e14ab3aac89", "2023-01-01", "2023-02-01", Status.IN_PROGRESS, "Custom web development project", "John Doe", "john@example.com", "1234567890", "123 Main St"));
        orderRepository.save(new Order("auth0|65702e81e9661e14ab3aac89", "2023-02-01", "2023-03-01", Status.COMPLETED, "Logo design for new brand", "Jane Smith", "jane@example.com", "9876543210", "456 Oak St"));
        orderRepository.save(new Order("auth0|65702e81e9661e14ab3aac89", "2023-03-01", "2023-04-01", Status.PENDING, "Social media marketing campaign", "Bob Johnson", "bob@example.com", null, "789 Pine St"));
        orderRepository.save(new Order("auth0|65702e81e9661e14ab3aac89", "2023-04-01", "2023-05-01", Status.CANCELLED, "iOS app development", "Alice Williams", "alice@example.com", "5432109876", "101 Elm St"));
        orderRepository.save(new Order("auth0|65702e81e9661e14ab3aac89", "2023-05-01", "2023-06-01", Status.COMPLETED, "SEO optimization for website", "David Brown", "david@example.com", "9876123450", "202 Maple St"));
    }

    @Test
    public void whenCompletedOrdersWithOneITemPerPageExist_thenReturnAllCompletedOrders() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?status=completed&pageSize=1&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }
    @Test
    public void whenCompleteOrdersWithoutPaginationExist_thenReturnAllCompletedOrders() {
        //arrange
        Integer expectedNumOrders = 2;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?status=completed&pageSize=0&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }
    @Test
    public void whenOrdersInProgressExist_thenReturnAllOrdersInProgress() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?status=in_progress&pageSize=50&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    public void whenOrdersWithoutStatusFilterExist_thenReturnAllOrders() {
        //arrange
        Integer expectedNumOrders = 5;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?pageSize=50&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    public void whenOrderWithoutStatusFilterNorPaginationExist_thenReturnAllOrders() {
        //arrange
        Integer expectedNumOrders = 5;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?pageSize=0&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

}
