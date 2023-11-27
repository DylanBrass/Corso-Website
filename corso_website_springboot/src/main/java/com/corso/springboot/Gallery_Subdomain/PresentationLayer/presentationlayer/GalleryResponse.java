package com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer.GalleryIdentifier;
import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
public class GalleryResponse {
    private String galleryId;
    private String description;
    private String photo;
    private int image_position;

}
