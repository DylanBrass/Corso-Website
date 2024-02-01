package com.corso.springboot.Gallery_Subdomain.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class GalleryIdentifier {

    private String galleryId;

    public GalleryIdentifier() {
        this.galleryId = java.util.UUID.randomUUID().toString();
    }


    public String getGalleryId() {
        return galleryId;
    }
}
