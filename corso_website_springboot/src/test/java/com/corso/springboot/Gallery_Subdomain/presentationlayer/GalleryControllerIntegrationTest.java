package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql({"/data-h2.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GalleryControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/corso/galleries";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    GalleryRepository galleryRepository;


    @Test
    public void whenGalleryExists_thenReturnAllGalleries() {
        //arrange
        Integer expectedNumGalleries = 5;


        //act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES + "/carousel")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.length()").isEqualTo(expectedNumGalleries);
    }

}