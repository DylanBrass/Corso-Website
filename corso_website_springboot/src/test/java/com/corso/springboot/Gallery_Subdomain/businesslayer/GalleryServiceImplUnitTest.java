package com.corso.springboot.Gallery_Subdomain.businesslayer;

import com.corso.springboot.Gallery_Subdomain.datalayer.Gallery;
import com.corso.springboot.Gallery_Subdomain.datalayer.GalleryRepository;
import com.corso.springboot.Gallery_Subdomain.datamapperlayer.GalleryResponseMapper;
import com.corso.springboot.Gallery_Subdomain.presentationlayer.GalleryResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GalleryServiceImplUnitTest {

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private GalleryResponseMapper galleryResponseMapper;

    @InjectMocks
    private GalleryServiceImpl galleryService;

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
}
