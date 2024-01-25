package com.corso.springboot.Gallery_Subdomain.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    public void whenFindById_thenReturnGallery(){
        Gallery foundGallery = galleryRepository.getGalleryByGalleryId_GalleryId(preSavedGallery.getGalleryId().getGalleryId());

        assert(foundGallery.getGalleryId().getGalleryId().equals(preSavedGallery.getGalleryId().getGalleryId()));
    }


}