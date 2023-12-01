package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GalleryRequest {
    String description;
    String photo;
    int image_position;

}
