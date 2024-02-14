package com.corso.springboot.Gallery_Subdomain.businesslayer;

import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryOrderUpdateRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;

import java.io.IOException;
import java.util.List;

public interface GalleryService {

    List<GalleryResponse> getGallery();

    List<GalleryResponse> updateGalleryOrder(List<GalleryOrderUpdateRequest> galleryOrderUpdateRequest);

    Integer getGalleryCount();

    GalleryResponse createGallery(GalleryRequest galleryRequest) throws IOException;

    GalleryResponse modifyGallery(String galleryId, GalleryRequest galleryRequest) throws IOException;

    GalleryResponse getGalleryById(String galleryId);

    void deleteGalleryById(String galleryId) throws IOException;
}
