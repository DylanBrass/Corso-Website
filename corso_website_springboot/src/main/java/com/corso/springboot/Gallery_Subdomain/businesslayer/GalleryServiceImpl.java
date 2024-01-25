package com.corso.springboot.Gallery_Subdomain.businesslayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryIdentifier;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import com.corso.springboot.Gallery_Subdomain.datamapperlayer.GalleryRequestMapper;
import com.corso.springboot.Gallery_Subdomain.datamapperlayer.GalleryResponseMapper;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryOrderUpdateRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;
import com.corso.springboot.cloudinary.CloudinaryService;
import com.corso.springboot.utils.exceptions.Gallery.GalleryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService{

    private final GalleryRepository galleryRepository;

    private final GalleryResponseMapper galleryResponseMapper;

    private final GalleryRequestMapper galleryRequestMapper;

    private final CloudinaryService cloudinary;


    @Override
    public List<GalleryResponse> getGallery() {
        log.info("GalleryServiceImpl.getGallery() called");
        return galleryResponseMapper.toGalleriesResponse(galleryRepository.findAll());
    }

    @Override
    public List<GalleryResponse> updateGalleryOrder(List<GalleryOrderUpdateRequest> updateRequests) {
            List<Gallery> updatedGalleries = new ArrayList<>();

            for (GalleryOrderUpdateRequest updateRequest : updateRequests) {
                Gallery galleryToUpdate = galleryRepository.getGalleryByGalleryId_GalleryId(updateRequest.getGalleryId());

                if (galleryToUpdate == null){
                    throw new GalleryNotFoundException("Gallery not found");
                }

                Gallery updatedGallery = galleryRequestMapper.toGalleryOrderUpdateRequest(updateRequest);

                updatedGallery.setId(galleryToUpdate.getId());
                updatedGallery.setGalleryId(galleryToUpdate.getGalleryId());
                updatedGallery.setDescription(galleryToUpdate.getDescription());
                updatedGallery.setPhoto(galleryToUpdate.getPhoto());
                updatedGallery.setImage_position(updateRequest.getImage_position());
                updatedGalleries.add(updatedGallery);
            }

            return galleryResponseMapper.toGalleriesResponse(galleryRepository.saveAll(updatedGalleries));
    }

    @Override
    public Integer getGalleryCount() {
        return galleryRepository.countGalleryBy();
    }

    @Override
    public GalleryResponse createGallery(GalleryRequest galleryRequest) throws IOException {

        String imageUrl = cloudinary.uploadBase64Image(galleryRequest.getPhoto());

        log.info("GalleryServiceImpl.createGallery() called");
        log.info("URL: " + imageUrl);

        Gallery gallery = galleryRequestMapper.toGalleryOrderRequest(galleryRequest);

        gallery.setGalleryId(new GalleryIdentifier());

        gallery.setPhoto(imageUrl);

        gallery.setImage_position(getGalleryCount() + 1);

        return galleryResponseMapper.toGalleryResponse(galleryRepository.save(gallery));
    }
}
