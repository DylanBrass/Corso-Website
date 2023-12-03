package com.corso.springboot.Gallery_Subdomain.presentationlayer;

import com.corso.springboot.Gallery_Subdomain.businesslayer.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PostMapping
    public String CreateGallery(){
        return "Testing authentication and authorization";
    }

    @PutMapping
    public String UpdateGallery(){
        return "Testing authentication";
    }

    @GetMapping
    public String GetGalleryById(@AuthenticationPrincipal OidcUser user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return "Hello, " + user.getFullName() + "!<br/><br/>Authorities: " + authorities;
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('Admin')")
    public String Test(@AuthenticationPrincipal OidcUser principal){
        log.info(principal.getClaims().toString());
        log.info(principal.getAuthorities().toString());

        return "Testing authentication and authorization <a href='http://localhost:8080/logout'>Logout</a>";
    }
}
