package com.corso.springboot.Gallery_Subdomain.datalayer;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Table(name = "gallery")
@AllArgsConstructor
@Entity
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private GalleryIdentifier galleryId;

    private String description;

    private String photo;

    private int image_position;

    public Gallery(String description, String photo, int image_position) {
        this.galleryId = new GalleryIdentifier();
        this.description = description;
        this.photo = photo;
        this.image_position = image_position;
    }
}
