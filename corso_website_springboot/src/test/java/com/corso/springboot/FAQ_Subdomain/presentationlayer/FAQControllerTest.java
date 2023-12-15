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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class FAQControllerTest {

    private final String BASE_URI_FAQS = "/api/v1/corso/FAQs";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    FAQRepository faqRepository;

    @BeforeEach
    public void setUp() {
        faqRepository.deleteAll();
        faqRepository.save(new FAQ(1, new FAQIdentifier(), "How do I create an account", "Top right corner", true));
        faqRepository.save(new FAQ(2, new FAQIdentifier(), "How do I create an account", "Top right corner", false));
        faqRepository.save(new FAQ(3, new FAQIdentifier(), "How do I create an account", "Top right corner", true));
    }

    @Test
    void GetAllFAQs_ThenReturnAllFAQS() {
            //act
            webTestClient.get()
                    .uri(BASE_URI_FAQS)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(FAQ.class)
                    .value(faqs -> assertEquals(3, faqs.size()));
        }


        //FAQ by Id
    @Test
    void getPreferedFAQsById_ThenReturnPreferedFAQById() {
        //arrange
        Integer faqId = 1;
        //act
        webTestClient.get()
                .uri(BASE_URI_FAQS + "/" + faqId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.faqId").isEqualTo(faqId);
    }
}