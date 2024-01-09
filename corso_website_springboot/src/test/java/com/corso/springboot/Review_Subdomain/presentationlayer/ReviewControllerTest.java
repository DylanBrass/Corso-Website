package com.corso.springboot.Review_Subdomain.presentationlayer;

import com.corso.springboot.FAQ_Subdomain.datalayer.FAQRepository;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReviewControllerTest {


    private final String BASE_URI_REVIEWS = "/api/v1/corso/reviews";

    @Autowired
    WebTestClient webTestClient;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void getAllReviews_ReturnAllReviews() {
        //act
        webTestClient.get()
                .uri(BASE_URI_REVIEWS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(5);
    }

    @Test
    void getAllReviews_ReturnAllReviewsByCustomerFullName() {
        //act
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/search?customerFullName=John Doe")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(1);
    }


    @Test
    void getReviewsByCustomerFullName_NullParameter_ReturnsBadRequest() throws Exception {
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/search?customerFullName=")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().isEmpty();
    }
}