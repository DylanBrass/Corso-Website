package com.corso.springboot.Review_Subdomain.presentationlayer;

import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = true)
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
    @WithMockUser(authorities = "Admin")
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
    @WithMockUser(authorities = "Admin")
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
    @WithMockUser(authorities = "Admin")
    void getReviewsByCustomerFullName_NullParameter_ReturnsBadRequest() throws Exception {
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/search?customerFullName=")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().isEmpty();
    }

    @Test
    void getPinnedReviews_ThenReturnPinnedReviews() {
        //arrange
        Integer expectedNumberOFReviews = 2;
        //act
        webTestClient.get()
                .uri(BASE_URI_REVIEWS + "/pinned")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumberOFReviews);
    }

    @Test
    void getReviewsByUserId_ThenReturnReviewsByUserId() throws Exception {
        //arrange
        Integer expectedNumberOfReviews = 5;
        //act

        mockMvc.perform(get(BASE_URI_REVIEWS + "/user")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(expectedNumberOfReviews));
    }

    @Test
    void createReviewByCustomer_ThenReturnReview() throws Exception {
        //arrange
        // act
        mockMvc.perform(post(BASE_URI_REVIEWS)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewRating\": 5," +
                                " \"message\": \"I had a great experience with Corso Electric, they were very professional and courteous. I would recommend them to anyone!\", " +
                                "\"customerFullName\": \"John Doe\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewRating")
                        .value(5))
                .andExpect(jsonPath("$.message")
                        .value("I had a great experience with Corso Electric, they were very professional and courteous. I would recommend them to anyone!"))
                .andExpect(jsonPath("$.customerFullName")
                        .value("John Doe"));

        mockMvc.perform(post(BASE_URI_REVIEWS)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewRating\": 5," +
                                " \"message\": \"I had a great experience with Corso Electric, they were very professional and courteous. I would recommend them to anyone!\", " +
                                "\"customerFullName\": \"John Doe\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("You have already left a review this week!"));

    }

    @Test
    void postReviewWithRatingGreaterThenFive() throws Exception {
        //arrange
        //act
        mockMvc.perform(post(BASE_URI_REVIEWS)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewRating\": 6," +
                                " \"message\": \"I had a great experience with Corso Electric, they were very professional and courteous. I would recommend them to anyone!\", " +
                                "\"customerFullName\": \"John Doe\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Review rating must be between 1 and 5"));
    }
}