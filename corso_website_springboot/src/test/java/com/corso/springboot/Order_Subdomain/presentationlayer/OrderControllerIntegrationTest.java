package com.corso.springboot.Order_Subdomain.presentationlayer;


import com.corso.springboot.Order_Subdomain.datalayer.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @WithMockUser(authorities = "Admin")
    public void countTotalOfOrders() throws Exception {
        //arrange
        Integer expectedNumOrders = 5;

        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/all/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(expectedNumOrders);

    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenAllOrdersExist_thenReturnAllOrdersWithoutPagination() throws Exception {
        //arrange
        Integer expectedNumOrders = 5;

        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?pageSize=0&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);

    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenAllOrdersExist_thenReturnAllOrdersWithPagination() throws Exception {
        //arrange
        Integer expectedNumOrders = 1;

        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?pageSize=1&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);

    }
    @Test
    @WithMockUser(authorities = "Admin")
    public void whenAllOrdersExist_thenReturnAllOrdersWithPaginationAndStatusFilter() throws Exception {
        //arrange
        Integer expectedNumOrders = 1;

        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?status=completed&pageSize=1&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);

    }


    @Test
    @WithMockUser(authorities = "Admin")
    public void whenCompletedOrdersWithOneITemPerPageExist_thenReturnAllCompletedOrders() throws Exception {
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
    @WithMockUser(authorities = "Admin")
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
    @WithMockUser(authorities = "Admin")
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
    @WithMockUser(authorities = "Admin")
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
    @WithMockUser(authorities = "Admin")
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

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenOverdueOrdersExist_thenReturnOverdueOrders() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/overdue?pageSize=50&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenOverdueOrdersWithoutPaginationExist_thenReturnAllOverdueOrders() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/overdue?pageSize=0&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenOrderWithStatusFilterAndPaginationExist_thenReturnAllOrders() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "?status=pending&pageSize=1&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }
    @Test
    @WithMockUser(authorities = "Admin")
    public void whenCountByOrderStatus_thenReturnCount() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/manage/count?status=PENDING")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(expectedNumOrders);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void getOrdersByUserIdAndStatus_thenReturnTwoOrders() {
        //arrange
        Integer expectedNumOrders = 2;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?status=completed&pageSize=50&offset=0&userId=auth0|65702e81e9661e14ab3aac89&status=completed")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void getOrdersByUserId_thenReturnTwoOrders() {
        //arrange
        Integer expectedNumOrders = 5;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?&pageSize=10&offset=0&userId=auth0|65702e81e9661e14ab3aac89")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    public void getOrdersWithOnlyStatus_ShouldReturnOneOrder() {
        //arrange
        Integer expectedNumOrders = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_ORDERS + "/filter?status=pending&pageSize=10&offset=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumOrders);
    }



}
