package com.corso.springboot.Gallery_Subdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class GalleryIdentifier {

    private String galleryId;

    public GalleryIdentifier() {
        this.galleryId = java.util.UUID.randomUUID().toString();
    }


}
