package com.corso.springboot.Gallery_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class GalleryRepositoryPersistenceTest {

    @Autowired
    GalleryRepository galleryRepository;
    private Gallery preSavedGallery;

    @BeforeEach
    public void setup(){
        galleryRepository.deleteAll();
        preSavedGallery = galleryRepository.save(
                new Gallery("an image", "a description", 1));
    }



}