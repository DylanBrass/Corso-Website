package com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer.GalleryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
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