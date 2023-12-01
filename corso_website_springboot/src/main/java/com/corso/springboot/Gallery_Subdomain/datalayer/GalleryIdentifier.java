package com.corso.springboot.Gallery_Subdomain.datalayer;

import jakarta.persistence.Embeddable;

@Embeddable
public class GalleryIdentifier {

    private String galleryId;

    public GalleryIdentifier() {
        this.galleryId = java.util.UUID.randomUUID().toString();
    }

    /*
    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

     */

    public String getGalleryId() {
        return galleryId;
    }
}
