package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.businesslayer.GalleryService;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;


@CrossOrigin(origins = {"http://localhost:3000","https://corsoelectricdev.tech/"},allowCredentials = "true")
@RestController
@RequestMapping("api/v1/corso/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }
    @GetMapping("/carousel")
    public ResponseEntity<List<GalleryResponse>> GetGallery(){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.getGallery());
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PatchMapping("/carousel/order")
    public ResponseEntity<List<GalleryResponse>> updateGalleryOrder(@RequestBody List<GalleryOrderUpdateRequest> galleryOrderUpdateRequest){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.updateGalleryOrder(galleryOrderUpdateRequest));
    }

}
