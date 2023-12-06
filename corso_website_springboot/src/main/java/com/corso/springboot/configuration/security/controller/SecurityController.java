package com.corso.springboot.configuration.security.controller;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@Generated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/corso/security")
public class SecurityController {
    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectAfterLogin(HttpServletRequest request, @AuthenticationPrincipal OidcUser principal){

        ResponseCookie cookie = ResponseCookie.from("id_token",principal.getIdToken().getTokenValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie authCookie = ResponseCookie.from("isAuthenticated", "true")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie accessPermissionCookie = ResponseCookie.from("accessPermission", principal.getAuthorities().toString().replace(",","-").replace(" ",""))
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .build();
        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, authCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessPermissionCookie.toString());

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                .location(URI.create("http://localhost:3000")).build();
    }


    @PostMapping("/test")
    public ResponseEntity<TestDTO> test(@AuthenticationPrincipal OidcUser principal, @RequestBody TestDTO body){
        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    @PostMapping("/test/admin")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Void> testAdmin(@AuthenticationPrincipal OidcUser principal){
        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
class TestDTO{
    private String name;
    private String surname;
}