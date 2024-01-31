package com.corso.springboot.Gallery_Subdomain.datamapperlayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryOrderUpdateRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface GalleryRequestMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "galleryId", ignore = true),
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "photo", ignore = true),
    })
    Gallery toGalleryOrderUpdateRequest(GalleryOrderUpdateRequest galleryOrderUpdateRequest);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "galleryId", expression = "java(new com.corso.springboot.Gallery_Subdomain.datalayer.GalleryIdentifier())"),
    })
    Gallery toGalleryRequest(GalleryRequest galleryRequest);
}
