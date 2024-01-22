package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MockMvc mockMvc;

    @Autowired
    GalleryRepository galleryRepository;


    @Test
    public void whenGalleryExists_thenReturnAllGalleries()  {
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

    @Test
    @WithMockUser(authorities = "Admin")
    public void whenGalleryCarouselChangesOrder_thenReturnUpdatedGalleries() throws Exception {

      mockMvc.perform(patch(BASE_URI_GALLERIES + "/carousel/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [{"galleryId":"e8da0dc7-eecc-47af-831e-ae6d5101cc24","image_position":1},
                                {"galleryId":"c3711305-3e95-4dce-aec5-c835cb662225","image_position":2},
                                {"galleryId":"ed948a48-bced-404e-bc55-6f983c1f304b","image_position":3},
                                {"galleryId":"cae88af2-8dd2-4087-bcd4-d863f60dda47","image_position":4},
                                {"galleryId":"62b068a9-ef01-4bfd-8ee4-437deb43c3f6","image_position":5}]""")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].image_position").value(1))
                .andExpect(jsonPath("$[1].image_position").value(2))
                .andExpect(jsonPath("$[2].image_position").value(3))
                .andExpect(jsonPath("$[3].image_position").value(4))
                .andExpect(jsonPath("$[4].image_position").value(5));

      assert galleryRepository.findAll().size() == 5;
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void getCountOfGalleries_ShouldReturnFive() {
        //arrange
        Integer expectedNumGalleries = 5;

        //act
        webTestClient.get()
                .uri(BASE_URI_GALLERIES + "/carousel/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$").isEqualTo(expectedNumGalleries);
    }

}