package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebClient
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class GalleryControllerIntegrationTest {
    private final String BASE_URI_GALLERIES = "/api/v1/corso/galleries";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    GalleryRepository galleryRepository;


    @Before
    public void setUp() {
        galleryRepository.save(Gallery.builder()
                .description("gallery1")
                .photo("photo1")
                .image_position(1)
                .build());

        galleryRepository.save(Gallery.builder()
                .description("gallery2")
                .photo("photo2")
                .image_position(2)
                .build());

        galleryRepository.save(Gallery.builder()
                .description("gallery3")
                .photo("photo3")
                .image_position(3)
                .build());

        galleryRepository.save(Gallery.builder()
                .description("gallery4")
                .photo("photo4")
                .image_position(4)
                .build());

        galleryRepository.save(Gallery.builder()
                .description("gallery5")
                .photo("photo5")
                .image_position(5)
                .build());


    }

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