package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Data
@Builder
@AllArgsConstructor
public class GalleryResponse {
    private String galleryId;
    private String description;
    private String photo;
    private int image_position;

}
