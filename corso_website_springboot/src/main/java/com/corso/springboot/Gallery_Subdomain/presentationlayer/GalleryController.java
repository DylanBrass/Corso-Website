package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.businesslayer.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectriqueinc.tech/"},allowCredentials = "true")
@RestController
@RequestMapping("api/v1/corso/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @Cacheable(value = "gallery", key = "#root.methodName",sync = true)
    @GetMapping("/carousel")
    public ResponseEntity<List<GalleryResponse>> GetGallery(){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.getGallery());
    }

    @CacheEvict(value = "gallery", allEntries = true)
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PatchMapping("/carousel/order")
    public ResponseEntity<List<GalleryResponse>> updateGalleryOrder(@RequestBody List<GalleryOrderUpdateRequest> galleryOrderUpdateRequest){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.updateGalleryOrder(galleryOrderUpdateRequest));
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @GetMapping("/carousel/count")
    public ResponseEntity<Integer> getGalleryCount(){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.getGalleryCount());
    }


    @CacheEvict(value = "gallery", allEntries = true)
    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/carousel")
    public ResponseEntity<GalleryResponse> createGallery(@RequestBody GalleryRequest galleryRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.createGallery(galleryRequest));
    }

}
