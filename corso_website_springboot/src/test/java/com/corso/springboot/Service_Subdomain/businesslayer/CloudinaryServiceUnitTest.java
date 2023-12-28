package com.corso.springboot.Service_Subdomain.businesslayer;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CloudinaryServiceUnitTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploaderMock;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Ensure that the cloudinary field is set in the CloudinaryService instance
        ReflectionTestUtils.setField(cloudinaryService, "cloudinary", cloudinary);

        // Mock the uploader() method
        when(cloudinary.uploader()).thenReturn(uploaderMock);
    }

    @Test
    void uploadBase64Image_ShouldSucceed() throws IOException {
        String base64Image = "data:image/png;base64," + getBase64ImageFromFile("src/test/resources/testService.jpg");

        // Mock Cloudinary response
        when(cloudinary.uploader()).thenReturn(uploaderMock);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "https://example.com/image.jpg");
        when(uploaderMock.upload(Mockito.any(byte[].class), Mockito.anyMap())).thenReturn(uploadResult);

        // Act
        String result = cloudinaryService.uploadBase64Image(base64Image);

        // Assert
        assertEquals("https://example.com/image.jpg", result);
    }

    private String getBase64ImageFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileContent = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    void uploadBase64Image_ShouldThrowException_WhenBase64ImageIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cloudinaryService.uploadBase64Image(null));
    }

    @Test
    void uploadBase64Image_ShouldThrowException_WhenBase64ImageIsEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cloudinaryService.uploadBase64Image(""));
    }

    @Test
    void uploadBase64Image_ShouldThrowException_WhenCloudinaryUploaderThrowsException() throws IOException {
        String base64Image = "data:image/png;base64," + getBase64ImageFromFile("src/test/resources/testService.jpg");

        // Mock Cloudinary response
        when(cloudinary.uploader()).thenReturn(uploaderMock);
        when(uploaderMock.upload(Mockito.any(byte[].class), Mockito.anyMap())).thenThrow(new IOException("Failed to upload"));

        // Act & Assert
        assertThrows(IOException.class, () -> cloudinaryService.uploadBase64Image(base64Image));
    }

    @Test
    void uploadBase64Image_ShouldThrowException_WhenCloudinaryUploaderReturnsNull() throws IOException {
        String base64Image = "data:image/png;base64," + getBase64ImageFromFile("src/test/resources/testService.jpg");

        // Mock Cloudinary response
        when(cloudinary.uploader()).thenReturn(uploaderMock);

        // Mock uploadResult to return a Map with a null value for "url"
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", null);
        when(uploaderMock.upload(Mockito.any(byte[].class), Mockito.anyMap())).thenReturn(uploadResult);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> cloudinaryService.uploadBase64Image(base64Image));
    }
}