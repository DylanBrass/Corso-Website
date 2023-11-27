package com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private GalleryIdentifier galleryId;
    private String description;
    private String photo;
    private int image_position;

}
