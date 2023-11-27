package com.corso.springboot.Gallery_Subdomain.PresentationLayer.datamapperlayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer.GalleryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GalleryResponseMapper {

    @Mapping(expression="java(gallery.getGalleryId().getGalleryId())", target = "galleryId")
    GalleryResponse toGalleryResponse(Gallery gallery);
    List<GalleryResponse> toGalleriesResponse(List<Gallery> galleries);

}
