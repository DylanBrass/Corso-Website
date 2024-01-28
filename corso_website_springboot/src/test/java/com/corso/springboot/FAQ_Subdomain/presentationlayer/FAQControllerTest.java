package com.corso.springboot.FAQ_Subdomain.presentationlayer;

import com.corso.springboot.FAQ_Subdomain.datalayer.FAQ;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQIdentifier;
import com.corso.springboot.FAQ_Subdomain.datalayer.FAQRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class FAQControllerTest {

    private final String BASE_URI_FAQS = "/api/v1/corso/faqs";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    FAQRepository faqRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void GetAllFAQs_ThenReturnAllFAQS() {
        //act
        webTestClient.get()
                .uri(BASE_URI_FAQS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(5);
    }


    @Test
    void getPreferedFAQsById_ThenReturnPreferedFAQById() {
        //arrange
        Integer expectedNumberOFFaqs = 3;
        //act
        webTestClient.get()
                .uri(BASE_URI_FAQS + "/preferred")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumberOFFaqs);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void chooseThreeFAQs_ThenReturnFAQs() throws Exception {
        mockMvc.perform(patch(BASE_URI_FAQS + "/viewable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                [
                            {
                                "faqId": "87e59aeb-3aa3-4c22-be1d-a35acde209b9",
                                "preference": true
                            },
                            {
                                "faqId": "368918a2-dbfe-4b50-bc6b-9ee3a7c5a0eb",
                                "preference": false
                            },
                            {
                                "faqId": "5b1ae831-a5cb-4d42-b3f3-af11b87b0f6b",
                                "preference": false
                            },
                            {
                                "faqId": "30c0e531-5679-40a6-aaf1-13e3e052230f",
                                "preference": true
                            },
                            {
                                "faqId": "753a87e5-201e-4d4d-bd67-8393bc9b88f7",
                                "preference": true
                            }
                            
                        ]
                        """)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$.[0].preference").value(true))
                .andExpect(jsonPath("$.[1].preference").value(false));

        assert faqRepository.findAll().size() == 5;
    }


    @Test
    @WithMockUser(authorities = "Admin")
    void viewFaqById_ThenReturnFAQById() {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b9";
        //act
        webTestClient.get()
                .uri(BASE_URI_FAQS + "/" + expectedFaqId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.faqid").isEqualTo(expectedFaqId)
                .jsonPath("$.question").isEqualTo("What services do you offer?")
                .jsonPath("$.answer").isEqualTo("We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.");
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void deleteFaqById_ThenReturnDeletedFAQById() {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b9";
        //act
        webTestClient.delete()
                .uri(BASE_URI_FAQS + "/" + expectedFaqId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.faqid").isEqualTo(expectedFaqId)
                .jsonPath("$.question").isEqualTo("What services do you offer?")
                .jsonPath("$.answer").isEqualTo("We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.");
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void deleteFaqById_ThenReturn404() {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b8";
        //act
        webTestClient.delete()
                .uri(BASE_URI_FAQS + "/" + expectedFaqId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void updateFAQ_ThenReturnUpdatedFAQ() throws Exception {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b9";
        //act
        mockMvc.perform(put(BASE_URI_FAQS + "/" + expectedFaqId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "faqid": "87e59aeb-3aa3-4c22-be1d-a35acde209b9",
                                    "question": "What services do you offer?",
                                    "answer": "We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.",
                                    "preference": true
                                }
                        """)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.faqid").value(expectedFaqId))
                .andExpect(jsonPath("$.question").value("What services do you offer?"))
                .andExpect(jsonPath("$.answer").value("We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization."))
                .andExpect(jsonPath("$.preference").value(true));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void updateFAQ_ThenReturn404() throws Exception {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b8";
        //act
        mockMvc.perform(put(BASE_URI_FAQS + "/" + expectedFaqId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "faqid": "87e59aeb-3aa3-4c22-be1d-a35acde209b9",
                                    "question": "What services do you offer?",
                                    "answer": "We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.",
                                    "preference": true
                                }
                        """)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getFAQCount_ThenReturnCount() {
        //arrange
        Integer expectedCount = 5;
        //act
        webTestClient.get()
                .uri(BASE_URI_FAQS + "/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(expectedCount);
    }




    @Test
    @WithMockUser(authorities = "Admin")
    void deleteFaqById_ThenReturnNotFound() {
        //arrange
        String expectedFaqId = "87e59aeb-3aa3-4c22-be1d-a35acde209b8";
        //act
        webTestClient.delete()
                .uri(BASE_URI_FAQS + "/" + expectedFaqId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void createFaq_ThenReturnCreatedFAQ() {
        //arrange
        FAQIdentifier faqIdentifier= new FAQIdentifier();
        //act
        webTestClient.post()
                .uri(BASE_URI_FAQS)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""

                        {
                    "question": "What services do you offer?",
                    "answer": "We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization."
                        }
                """)
                .exchange().expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.faqid").isNotEmpty()
                .jsonPath("$.question").isEqualTo("What services do you offer?")
                .jsonPath("$.answer").isEqualTo("We offer a range of services including web development, graphic design, digital marketing, mobile app development, and SEO optimization.");
    }

    }