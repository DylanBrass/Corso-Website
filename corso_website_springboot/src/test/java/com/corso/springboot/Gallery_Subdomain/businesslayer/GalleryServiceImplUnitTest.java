package com.corso.springboot.Gallery_Subdomain.businesslayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryIdentifier;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import com.corso.springboot.Gallery_Subdomain.datamapperlayer.GalleryRequestMapper;
import com.corso.springboot.Gallery_Subdomain.datamapperlayer.GalleryResponseMapper;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryOrderUpdateRequest;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;
import com.corso.springboot.utils.exceptions.Gallery.GalleryNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            galleryService.updateGalleryOrder(Arrays.asList(mockGalleryOrderUpdateRequest));
        } catch (Exception e) {
            assertEquals("Gallery not found", e.getMessage());
        }
    }


    @Test
    public void getGalleryByInvalidId_ThrowsNotFoundException(){
        //arrange
        String invalidId = "invalidId";
        Mockito.when(galleryRepository.getGalleryByGalleryId_GalleryId(invalidId)).thenReturn(null);

        //act & assert
        assertThrows(GalleryNotFoundException.class, () -> galleryService.getGalleryById(invalidId));
    }
}
