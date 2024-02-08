package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.models.VerifyEmailRequest;
import com.corso.springboot.configuration.security.service.Auth0LoginService;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Generated
@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"}, allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/security")
public class SecurityController {

    @Value("${frontend.url}")
    private String frontendDomain;

    @Value("${backend.url}")
    private String backendDomain;

    private final Auth0LoginService auth0LoginService;
    private final Auth0ManagementService auth0ManagementService;
    private final CustomerService customerService;

    @Value("${okta.oauth2.groupsClaim}")
    private String roleTokenLocation;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectAfterLogin(@AuthenticationPrincipal OidcUser principal, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        String accessToken;

        if (principal == null) {
            log.info("Principal is null");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(backendDomain + "oauth2/authorization/okta")).build();
        }

        log.info("Principal: " + principal);

        log.info("Principal subject: " + principal.getSubject());

        //handle apple, google and facebook login
        if (principal.getSubject().contains("apple") || principal.getSubject().contains("google-oauth2")
                || principal.getSubject().contains("facebook")) {
            return auth0LoginService.handleExternalLogin(principal);
        }

        if (principal.getSubject().contains("auth0")) {

            accessToken = auth0ManagementService.getAccessToken();


            boolean isVerified;

            try {
                isVerified = principal.getClaim("email_verified");
            } catch (NullPointerException e) {
                isVerified = false;
            }

            if (!principal.getClaims().containsKey(roleTokenLocation)
                    || principal.getClaim(roleTokenLocation).toString().equals("[]")) {
                log.info("No roles found, adding default role");

                if (!isVerified) {
                    log.info("User not verified, redirecting to verification page");
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(frontendDomain + "verify")).build();
                }

                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(frontendDomain + "logout")).build();
            }

            if (!isVerified) {
                log.info("User not verified, redirecting to verification page");
                auth0LoginService.sendVerificationEmail(principal.getSubject(), accessToken);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(frontendDomain + "verify")).build();
            }
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .location(URI.create(frontendDomain)).build();

        }

        log.info("Authorities: " + principal.getAuthorities().toString().replace(",", "-"));
        return auth0LoginService.getVoidResponseEntity(principal);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal OidcUser principal) throws IOException, InterruptedException {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


        String userId = principal.getSubject();

        return ResponseEntity.ok().body(auth0ManagementService.getUserInfo(userId));
    }


    @PostMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestBody VerifyEmailRequest verifyEmailRequest) throws IOException, InterruptedException {

        String token = verifyEmailRequest.getToken();
        String email = verifyEmailRequest.getEmail();


        if (token == null || email == null)
            return ResponseEntity.badRequest().build();


        customerService.verifyEmail(token, email);

        return ResponseEntity.ok().build();
    }

}
