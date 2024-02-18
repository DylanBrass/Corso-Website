package com.corso.springboot.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;


    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadBase64Image(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Unable to upload the image – it appears that the image is missing. Please ensure you include a valid image (.jpg, .jpeg, .png) before uploading.");
        }

        try {
            // Remove the Base64 prefix if present
            String base64ImageNoPrefix = base64Image.split(",")[1];

            // Decode the base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64ImageNoPrefix);

            // Upload the decoded bytes and retrieve the URL
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(decodedBytes, ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("secure_url");

            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("Something went wrong while trying to upload the picture on the internet. The system expected to get a special web link for the picture, but it didn't get one. The upload cannot be completed.");
            }

            return url;
        } catch (IOException e) {
            throw new IOException("Error while uploading the image to Cloudinary.", e);
        }
    }

    public String extractPublicIdFromCloudinaryUrl(String cloudinaryUrl) {
        // Extract the public ID from the Cloudinary URL
        int lastSlashIndex = cloudinaryUrl.lastIndexOf('/');
        int fileExtensionIndex = cloudinaryUrl.lastIndexOf('.');
        return cloudinaryUrl.substring(lastSlashIndex + 1, fileExtensionIndex);
    }

    public String updateCloudinaryImage(String publicId, String base64Image) throws IOException {
        try {
            // Decode the base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

            // Update the existing image in Cloudinary
            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(decodedBytes, ObjectUtils.asMap("public_id", publicId));
            String updatedUrl = (String) uploadResult.get("url");

            if (updatedUrl == null || updatedUrl.isEmpty()) {
                throw new IllegalStateException("Failed to update the picture in Cloudinary.");
            }

            return updatedUrl;
        } catch (IOException e) {
            throw new IOException("Error while updating the image in Cloudinary.", e);
        }
    }

    public void deleteCloudinaryImage(String publicId) throws IOException {
        try {

            // Deletes the image from Cloudinary
            @SuppressWarnings("rawtypes")
            Map deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String deletedUrl = (String) deleteResult.get("result");

            if (deletedUrl == null || deletedUrl.isEmpty()) {
                throw new IllegalStateException(deletedUrl + "Failed to delete the picture from Cloudinary.");
            }

        } catch (IOException e) {
            throw new IOException("Error while deleting the image from Cloudinary.", e);
        }
    }
}

