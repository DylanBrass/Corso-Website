package com.corso.springboot.Gallery_Subdomain.businesslayer;

import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;

import java.util.List;

public interface GalleryService {

    List<GalleryResponse> getGallery();
}
