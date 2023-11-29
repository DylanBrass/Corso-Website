package com.corso.springboot.Gallery_Subdomain.PresentationLayer.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.PresentationLayer.businesslayer.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin({"http://localhost:3000"})
@RestController
@RequestMapping("api/corso/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }
    @GetMapping("/carousel")
    public ResponseEntity<List<GalleryResponse>> GetGallery(){
        return ResponseEntity.status(HttpStatus.OK).body(galleryService.getGallery());
    }

}
