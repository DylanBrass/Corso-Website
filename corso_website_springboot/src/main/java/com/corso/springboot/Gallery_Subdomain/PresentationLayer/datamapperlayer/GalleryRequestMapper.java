package com.corso.springboot.Gallery_Subdomain.PresentationLayer.datamapperlayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer.GalleryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GalleryRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "galleryId", ignore = true)
    })
    Gallery toGallery(GalleryRequest galleryRequest);
    List<Gallery> toGalleries(List<GalleryRequest> galleryRequests);
}
