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


@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@Slf4j
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

//    @PostMapping
//    public String CreateGallery(){
//        return "Testing security and authorization";
//    }
//
//    @PutMapping
//    public String UpdateGallery(){
//        return "Testing security";
//    }
//
//    @GetMapping
//    public String GetGalleryById(){
//
//        return "Hello World";
//    }
//
//    @GetMapping("/test")
//    @PreAuthorize("hasAuthority('Admin')")
//    public String Test(@AuthenticationPrincipal OidcUser principal){
//        log.info(principal.getClaims().toString());
//        log.info(principal.getAuthorities().toString());
//
//        return "Testing security and authorization";
//    }
}
