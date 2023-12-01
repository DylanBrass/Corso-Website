package com.corso.springboot.Gallery_Subdomain.datamapperlayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GalleryResponseMapper {

    @Mapping(expression="java(gallery.getGalleryId().getGalleryId())", target = "galleryId")
    GalleryResponse toGalleryResponse(Gallery gallery);
    List<GalleryResponse> toGalleriesResponse(List<Gallery> galleries);

}
