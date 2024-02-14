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
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

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

            if (galleryToUpdate == null) {
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

        galleryRepository.saveAll(updatedGalleries);

        return galleryResponseMapper.toGalleriesResponse(reorderGallery());
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

        Gallery gallery = galleryRequestMapper.toGalleryRequest(galleryRequest);

        gallery.setGalleryId(new GalleryIdentifier());

        gallery.setPhoto(imageUrl);

        gallery.setImage_position(getGalleryCount() + 1);

        GalleryResponse galleryResponse = galleryResponseMapper.toGalleryResponse(galleryRepository.save(gallery));

        reorderGallery();

        return galleryResponse;
    }

    @Override
    public GalleryResponse modifyGallery(String galleryId, GalleryRequest galleryRequest) {
        try {
            Gallery existingGallery = galleryRepository.getGalleryByGalleryId_GalleryId(galleryId);

            if (existingGallery == null) {
                throw new GalleryNotFoundException("Gallery not found");
            }

            existingGallery.setDescription(galleryRequest.getDescription());

            if (galleryRequest.getPhoto() != null) {
                String existingPhotoPublicId = cloudinary.extractPublicIdFromCloudinaryUrl(existingGallery.getPhoto());

                if (!galleryRequest.getPhoto().contains("https://") && !galleryRequest.getPhoto().contains("http://")) {
                    existingGallery.setPhoto(cloudinary.updateCloudinaryImage(existingPhotoPublicId, galleryRequest.getPhoto()));
                }
            }

            return galleryResponseMapper.toGalleryResponse(galleryRepository.save(existingGallery));

        } catch (IOException e) {
            throw new RuntimeException("Failed to update images", e);
        }
    }

    @Override
    public GalleryResponse getGalleryById(String galleryId) {
        Gallery gallery = galleryRepository.getGalleryByGalleryId_GalleryId(galleryId);

        if (gallery == null) {
            throw new GalleryNotFoundException("Gallery not found");
        }

        return galleryResponseMapper.toGalleryResponse(gallery);
    }

    @Override
    public void deleteGalleryById(String galleryId) throws IOException {
        Gallery gallery = galleryRepository.getGalleryByGalleryId_GalleryId(galleryId);

        if (gallery == null) {
            throw new GalleryNotFoundException("Gallery not found");
        }

        String publicId = cloudinary.extractPublicIdFromCloudinaryUrl(gallery.getPhoto());

        cloudinary.deleteCloudinaryImage(publicId);

        galleryRepository.delete(gallery);

        reorderGallery();
    }


    public List<Gallery> reorderGallery() {
        List<Gallery> updatedGalleries = new ArrayList<>();

        galleryRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Gallery::getImage_position))
                .forEach(gallery -> {
                    gallery.setImage_position(updatedGalleries.size() + 1);
                    updatedGalleries.add(gallery);
                });


        return galleryRepository.saveAll(updatedGalleries);
    }

}