package com.corso.springboot.Gallery_Subdomain.PresentationLayer.businesslayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datalayer.GalleryRepository;
import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datamapperlayer.GalleryRequestMapper;
import com.corso.springboot.Gallery_Subdomain.PresentationLayer.datamapperlayer.GalleryResponseMapper;
import com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer.GalleryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService{

    private final GalleryRepository galleryRepository;
    private final GalleryResponseMapper galleryResponseMapper;

    private final GalleryRequestMapper galleryRequestMapper;


    @Override
    public List<GalleryResponse> getGallery() {
        return galleryResponseMapper.toGalleriesResponse(galleryRepository.findAll());
    }
}
