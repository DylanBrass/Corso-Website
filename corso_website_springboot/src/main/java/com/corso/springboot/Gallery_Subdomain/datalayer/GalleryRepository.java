package com.corso.springboot.Gallery_Subdomain.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, String> {

    Gallery getGalleryByGalleryId_GalleryId(String galleryId);

    Integer countGalleryBy();
}
