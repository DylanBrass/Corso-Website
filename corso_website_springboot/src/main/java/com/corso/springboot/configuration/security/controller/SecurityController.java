package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.security.service.Auth0Service;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Generated
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/security")
public class SecurityController {


    private final Auth0Service auth0Service;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectAfterLogin(@AuthenticationPrincipal OidcUser principal) throws IOException, InterruptedException, UnirestException {
        String accessToken;
        if (principal == null) {
            log.info("Principal is null");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:8080/oauth2/authorization/okta")).build();
        }


        //handle apple and google login
        if(principal.getSubject().contains("apple") || principal.getSubject().contains("google-oauth2")
                || principal.getSubject().contains("facebook")){
            return auth0Service.handleExternalLogin(principal);
        }

        if(principal.getSubject().contains("auth0")) {

            accessToken = auth0Service.getAccessToken();


            boolean isVerified;
            try {
                isVerified = principal.getClaim("email_verified");
            } catch (NullPointerException e) {
                isVerified = true;
            }

            if (!principal.getClaims().containsKey("https://corso-dev.com/roles")
                    || principal.getClaim("https://corso-dev.com/roles").toString().equals("[]")) {
                log.info("No roles found, adding default role");

                String userId = principal.getSubject().replace("|", "%7C");

                auth0Service.addRoleToUser(userId, accessToken);

                if (!isVerified) {
                    log.info("User not verified, redirecting to verification page");
                    auth0Service.sendVerificationEmail(principal.getSubject(), accessToken);
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("http://localhost:3000/verify")).build();
                }

                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("http://localhost:3000/logout")).build();

            }

            if (!isVerified) {
                log.info("User not verified, redirecting to verification page");
                auth0Service.sendVerificationEmail(principal.getSubject(), accessToken);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("http://localhost:3000/verify")).build();
            }
        }





        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        return auth0Service.getVoidResponseEntity(principal);
    }

    //todo: remove this method
    @PostMapping("/test")
    public ResponseEntity<TestDTO> test(@AuthenticationPrincipal OidcUser principal, @RequestBody TestDTO body){
        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


    //todo: remove this method
    @PostMapping("/test/admin")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<TestDTO> testAdmin(@AuthenticationPrincipal OidcUser principal, @RequestBody TestDTO body){
        log.info("Authorities: " + principal.getAuthorities().toString().replace(",","-"));
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}

//todo: remove this class
@EqualsAndHashCode(callSuper=false)
@Data
@Generated
@AllArgsConstructor
class TestDTO{
    private String name;
    private String surname;
}