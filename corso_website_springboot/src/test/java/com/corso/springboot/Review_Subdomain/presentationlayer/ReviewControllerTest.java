package com.corso.springboot.Review_Subdomain.presentationlayer;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Customer_Subdomain.presentationlayer.CustomerResponse;
import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Review_Subdomain.datalayer.ReviewRepository;
import com.corso.springboot.email.businessLayer.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {


    private final String BASE_URI_REVIEWS = "/api/v1/corso/reviews";

    @Autowired
    WebTestClient webTestClient;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ReviewRepository reviewRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getAllReviews_ReturnAllReviews() {
        //act
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASE_URI_REVIEWS)
                        .queryParam("pageSize", 10)
                        .queryParam("offset", 0)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(5);
    }

    @Test
    @WithMockUser(authorities = "Admin")
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
/*
    @Test
    void getReviewById_ThenReturnReviewById_BadRequest() throws Exception {
        //arrange
        String reviewId = "a";
        //act
        mockMvc.perform(get(BASE_URI_REVIEWS + "/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

 */

    @Test
    void getPinnedReviewsCount_ThenReturnPinnedReviewsCount() throws Exception {
        //arrange
        Integer expectedNumberOfReviews = 2;
        //act
        mockMvc.perform(get(BASE_URI_REVIEWS + "/count/pinned")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(expectedNumberOfReviews));
    }


    @Test
    void getAllReviewsCount_ThenReturnAllReviewsCount() throws Exception {
        //arrange
        Integer expectedNumberOfReviews = 5;
        //act
        mockMvc.perform(get(BASE_URI_REVIEWS + "/count")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(expectedNumberOfReviews));
    }


    @Test
    void getAllReviewsCount_ThenReturnAllReviewsCount_NotFound() throws Exception {
        //arrange
        Integer expectedNumberOfReviews = 5;
        //act
        mockMvc.perform(get(BASE_URI_REVIEWS + "/count")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                        .value(expectedNumberOfReviews));
    }

/*
    @Test
    void getReviewById_ThenReturnReviewById_NotFound() throws Exception {
        // Arrange
        int reviewId = 100;

        // Act and Assert
        mockMvc.perform(get(BASE_URI_REVIEWS + "/" + reviewId)
                .with(csrf())
                .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                        .authorities(new SimpleGrantedAuthority("Admin")))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }

 */


    @Test
    void updateReviewByIdChangePinnedStatus_ShouldSucceed() throws Exception {
        //arrange
        String reviewId = "7dcca21e-4d7b-4e8c-8c58-1a044acba519";

        CustomerResponse customerResponse = CustomerResponse.builder()
                .userId("auth0|65702e81e9661e14ab3aac89")
                .email("email@email.com")
                .build();

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(200);

        Mockito.when(customerService.getCustomerByUserId(any(String.class))).thenReturn(customerResponse);



        //act
        mockMvc.perform(patch(BASE_URI_REVIEWS + "/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"pinned\": true\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pinned").value(true));
    }

    @Test
    void viewReviewByReviewIdAndUserId_ShouldSucceed() throws Exception {
        //arrange
        String reviewId = "7dcca21e-4d7b-4e8c-8c58-1a044acba519";

        //act
        mockMvc.perform(get(BASE_URI_REVIEWS + "/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(reviewId))
                .andExpect(jsonPath("$.pinned").value(false))
                .andExpect(jsonPath("$.reviewRating").value(5));

    }

    @Test
    void deleteReviewByReviewIdAndUserId_ShouldSucceed() throws Exception {
        //arrange
        String reviewId = "7dcca21e-4d7b-4e8c-8c58-1a044acba519";

        //act
        mockMvc.perform(delete(BASE_URI_REVIEWS + "/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }




}