package com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GalleryRepository extends JpaRepository<Gallery, String> {

    Gallery getGalleryByGalleryId_GalleryId(String galleryId);
}
