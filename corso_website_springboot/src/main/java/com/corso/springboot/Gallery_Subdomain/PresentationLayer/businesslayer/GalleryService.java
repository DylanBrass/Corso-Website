package com.corso.springboot.Gallery_Subdomain.PresentationLayer.businesslayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer.GalleryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GalleryService {

    List<GalleryResponse> getGallery();
}
