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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class GalleryServiceImplUnitTest {

    @InjectMocks
    private GalleryServiceImpl galleryService;

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private GalleryResponseMapper galleryResponseMapper;

    @Mock
    private GalleryRequestMapper galleryRequestMapper;

    @Mock
    private CloudinaryService cloudinaryService;


    @Test
    void getAllGalleries_ShouldSucceed() {
        // Arrange
        List<GalleryResponse> mockGalleryResponses = Arrays.asList(
                new GalleryResponse("1", "Test Gallery 1", "Test Gallery 1 Description", 1),
                new GalleryResponse("2", "Test Gallery 2", "Test Gallery 2 Description", 2)
        );

        Mockito.when(galleryRepository.findAll()).thenReturn(Arrays.asList(
                new Gallery("Test Gallery 1", "Test Gallery 1 Description", 1),
                new Gallery("Test Gallery 2", "Test Gallery 2 Description", 2)));

        Mockito.when(galleryResponseMapper.toGalleriesResponse(Mockito.anyList())).thenReturn(mockGalleryResponses);

        // Act
        List<GalleryResponse> result = galleryService.getGallery();

        // Assert
        assertEquals(mockGalleryResponses, result);
        Mockito.verify(galleryRepository, Mockito.times(1)).findAll();
        Mockito.verify(galleryResponseMapper, Mockito.times(1)).toGalleriesResponse(Mockito.anyList());
    }
    @Test
    void updateGalleryOrder_ShouldSucceed() {
        // Arrange
        List<GalleryOrderUpdateRequest> updateRequests = new ArrayList<>();
        GalleryOrderUpdateRequest mockGalleryOrderUpdateRequest = GalleryOrderUpdateRequest.builder()
                .galleryId("1")
                .image_position(1)
                .build();

        GalleryOrderUpdateRequest mockGalleryOrderUpdateRequest2 = GalleryOrderUpdateRequest.builder()
                .galleryId("2")
                .image_position(2)
                .build();

        updateRequests.add(mockGalleryOrderUpdateRequest);
        updateRequests.add(mockGalleryOrderUpdateRequest2);

        List<Gallery> mockUpdatedGalleries = new ArrayList<>();
        Gallery mockUpdatedGallery = Gallery.builder()
                .galleryId(new GalleryIdentifier())
                .description("Test Gallery 1")
                .photo("Test Gallery 1 Description")
                .image_position(1).build();

        Gallery mockUpdatedGallery2 = Gallery.builder()
                .galleryId(new GalleryIdentifier())
                .description("Test Gallery 2")
                .photo("Test Gallery 2 Description")
                .image_position(1).build();

        mockUpdatedGalleries.add(mockUpdatedGallery);
        mockUpdatedGalleries.add(mockUpdatedGallery2);

        List<GalleryResponse> mockUpdatedResponses = new ArrayList<>();
        GalleryResponse mockUpdatedResponse = GalleryResponse.builder()
                .galleryId("1")
                .description("Test Gallery 1")
                .photo("Test Gallery 1 Description")
                .image_position(1).build();

        GalleryResponse mockUpdatedResponse2 = GalleryResponse.builder()
                .galleryId("2")
                .description("Test Gallery 2")
                .photo("Test Gallery 2 Description")
                .image_position(1).build();

        mockUpdatedResponses.add(mockUpdatedResponse);
        mockUpdatedResponses.add(mockUpdatedResponse2);

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(new Gallery("Test Gallery 1", "Test Gallery 1 Description", 1));
        Mockito.when(galleryRequestMapper.toGalleryOrderUpdateRequest(Mockito.any(GalleryOrderUpdateRequest.class))).thenReturn(new Gallery("Test Gallery 1", "Test Gallery 1 Description", 1));

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(new Gallery("Test Gallery 2", "Test Gallery 2 Description", 2));
        Mockito.when(galleryRequestMapper.toGalleryOrderUpdateRequest(Mockito.any(GalleryOrderUpdateRequest.class))).thenReturn(new Gallery("Test Gallery 2", "Test Gallery 2 Description", 2));

        Mockito.when(galleryRepository.saveAll(Mockito.anyList())).thenReturn(mockUpdatedGalleries);
        Mockito.when(galleryResponseMapper.toGalleriesResponse(Mockito.anyList())).thenReturn(mockUpdatedResponses);

        // Act
        List<GalleryResponse> result = galleryService.updateGalleryOrder(updateRequests);

        // Assert
        assertEquals(mockUpdatedResponses, result);
        assertEquals(mockUpdatedGalleries.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(mockUpdatedGalleries.get(0).getPhoto(), result.get(0).getPhoto());
        assertEquals(mockUpdatedGalleries.get(0).getImage_position(), result.get(0).getImage_position());
        assertEquals(mockUpdatedGalleries.get(1).getDescription(), result.get(1).getDescription());
        assertEquals(mockUpdatedGalleries.get(1).getPhoto(), result.get(1).getPhoto());
        assertEquals(mockUpdatedGalleries.get(1).getImage_position(), result.get(1).getImage_position());
        assertEquals(mockUpdatedResponses.get(0).getDescription(), result.get(0).getDescription());
    }

    @Test
    void updateGalleryOrder_ShouldThrowGalleryNotFoundException() {
        // Arrange
        GalleryOrderUpdateRequest mockGalleryOrderUpdateRequest = GalleryOrderUpdateRequest.builder()
                .galleryId("1")
                .image_position(1)
                .build();

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(null);

        // Act & Assert
        try {
            galleryService.updateGalleryOrder(Collections.singletonList(mockGalleryOrderUpdateRequest));
        } catch (Exception e) {
            assertEquals("Gallery not found", e.getMessage());
        }
    }

    @Test
    void modifyGallery_ShouldSucceed() throws Exception {
        // Arrange
        GalleryRequest galleryRequest = GalleryRequest.builder()
                .description("Test Gallery 1")
                .photo("Test Gallery 1 Description")
                .image_position(1)
                .build();

        Gallery gallery = Gallery.builder()
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        GalleryResponse galleryResponse = GalleryResponse.builder()
                .galleryId("62b068a9-ef01-4bfd-8ee4-437deb43c3f6")
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(gallery);
        Mockito.when(galleryRequestMapper.toGalleryRequest(Mockito.any(GalleryRequest.class))).thenReturn(gallery);
        Mockito.when(cloudinaryService.extractPublicIdFromCloudinaryUrl(Mockito.anyString())).thenReturn("public_id");
        Mockito.when(cloudinaryService.updateCloudinaryImage(Mockito.anyString(), Mockito.anyString())).thenReturn("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg");
        Mockito.when(galleryRepository.save(Mockito.any(Gallery.class))).thenReturn(gallery);
        Mockito.when(galleryResponseMapper.toGalleryResponse(Mockito.any(Gallery.class))).thenReturn(galleryResponse);

        // Act
        GalleryResponse result = galleryService.modifyGallery("62b068a9-ef01-4bfd-8ee4-437deb43c3f6", galleryRequest);

        // Assert
        assertEquals(galleryResponse, result);
        assertEquals(galleryResponse.getGalleryId(), result.getGalleryId());
        assertEquals(galleryResponse.getDescription(), result.getDescription());
        assertEquals(galleryResponse.getPhoto(), result.getPhoto());
        assertEquals(galleryResponse.getImage_position(), result.getImage_position());

    }

    @Test
    public void modifyGallery_ThrowsRuntimeException() throws IOException {
        // Arrange
        GalleryRequest galleryRequest = GalleryRequest.builder()
                .description("Test Gallery 1")
                .photo("Test Gallery 1 Description")
                .image_position(1)
                .build();

        Gallery gallery = Gallery.builder()
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        GalleryResponse galleryResponse = GalleryResponse.builder()
                .galleryId("62b068a9-ef01-4bfd-8ee4-437deb43c3f6")
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(gallery);
        Mockito.when(galleryRequestMapper.toGalleryRequest(Mockito.any(GalleryRequest.class))).thenReturn(gallery);
        Mockito.when(cloudinaryService.extractPublicIdFromCloudinaryUrl(Mockito.anyString())).thenReturn("public_id");
        Mockito.when(cloudinaryService.updateCloudinaryImage(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException("Simulated IOException"));
        Mockito.when(galleryRepository.save(Mockito.any(Gallery.class))).thenReturn(gallery);
        Mockito.when(galleryResponseMapper.toGalleryResponse(Mockito.any(Gallery.class))).thenReturn(galleryResponse);

        //act & assert
        assertThrows(RuntimeException.class, () -> galleryService.modifyGallery("62b068a9-ef01-4bfd-8ee4-437deb43c3f6", galleryRequest));
    }


    @Test
    public void getGalleryByInvalidId_ThrowsNotFoundException(){
        //arrange
        String invalidId = "invalidId";
        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(invalidId)).thenReturn(null);

        //act & assert
        assertThrows(GalleryNotFoundException.class, () -> galleryService.getGalleryById(invalidId));
    }

    @Test
    public void deleteGalleryByInvalidId_ThrowsNotFoundException() throws IOException {
        //arrange
        String invalidId = "invalidId";
        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(invalidId)).thenReturn(null);

        //act & assert
        assertThrows(GalleryNotFoundException.class, () -> galleryService.deleteGalleryById(invalidId));
    }

    @Test
    public void deleteGallery_ShouldSucceed() throws IOException {

        // Mock dependencies
        GalleryRepository galleryRepository = Mockito.mock(GalleryRepository.class);
        CloudinaryService cloudinaryService = Mockito.mock(CloudinaryService.class);
        GalleryResponseMapper galleryResponseMapper = Mockito.mock(GalleryResponseMapper.class);
        GalleryRequestMapper galleryRequestMapper = Mockito.mock(GalleryRequestMapper.class);

        GalleryServiceImpl galleryService = new GalleryServiceImpl(galleryRepository, galleryResponseMapper, galleryRequestMapper, cloudinaryService);

        GalleryIdentifier galleryIdentifier = new GalleryIdentifier();
        //arrange
        Gallery gallery = Gallery.builder()
                .galleryId(galleryIdentifier)
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        GalleryResponse galleryResponse = GalleryResponse.builder()
                .galleryId("62b068a9-ef01-4bfd-8ee4-437deb43c3f6")
                .description("Test Gallery 1")
                .photo("https://res.cloudinary.com/dszhbawv7/image/upload/v1701099721/test-image-carousel-5_jhydhp.jpg")
                .image_position(1)
                .build();

        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(Mockito.anyString())).thenReturn(gallery);
        Mockito.when(cloudinaryService.extractPublicIdFromCloudinaryUrl(Mockito.anyString())).thenReturn("public_id");
        Mockito.when(galleryRepository.save(Mockito.any(Gallery.class))).thenReturn(gallery);
        Mockito.when(galleryResponseMapper.toGalleryResponse(Mockito.any(Gallery.class))).thenReturn(galleryResponse);

        galleryService.deleteGalleryById("someGalleryId");

        Mockito.verify(cloudinaryService).deleteCloudinaryImage(Mockito.anyString());
    }

}
