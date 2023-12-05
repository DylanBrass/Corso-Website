package com.corso.springboot.Gallery_Subdomain.datamapperlayer;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GalleryRequestMapper {
/*
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "galleryId", ignore = true)
    })
    Gallery toGallery(GalleryRequest galleryRequest);
    List<Gallery> toGalleries(List<GalleryRequest> galleryRequests);

 */
}
