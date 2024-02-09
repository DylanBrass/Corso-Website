package com.corso.springboot.configuration.security.service;

import com.corso.springboot.utils.Tools.BasicTools;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
@Generated
@RequiredArgsConstructor
public class Auth0LoginService {

    @Value("${okta.oauth2.issuer}")
    private String issuer;


    @Value("${frontend.url}")
    private String frontendDomain;


    @Value("${okta.oauth2.groupsClaim}")
    private String roleTokenLocation;

    private final BasicTools basicTools;

    public void sendVerificationEmail(String userId, String accessToken) {
        log.info("Sending verification email");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "api/v2/jobs/verification-email"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString("{ \"user_id\": \"" + userId + "\" }"))
                .build();

        log.info("Request: " + request.toString());

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        assert response != null;
        log.info("Response: " + response.body());
    }


    public ResponseEntity<Void> handleExternalLogin(OidcUser principal) {
        log.info("Handling external Login");

        log.info("Principal: " + principal.getIdToken());

        if (!principal.getClaims().containsKey(roleTokenLocation)
                || principal.getClaim(roleTokenLocation).toString().equals("[]")) {
            log.info("No roles found, adding default role");

            

            return ResponseEntity.status(302)
                    .location(URI.create(frontendDomain+"external")).build();

        }

        return getVoidResponseEntity(principal);
    }


    public ResponseEntity<Void> getVoidResponseEntity(@AuthenticationPrincipal OidcUser principal) {
        HttpHeaders headers = new HttpHeaders();

        String formattedDomain = basicTools.getFormattedDomain();

                log.info("Formatted domain: " + formattedDomain);

        ResponseCookie authCookie = ResponseCookie.from("isAuthenticated", "true")
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie accessPermissionCookie = ResponseCookie.from("accessPermission", principal.getAuthorities().toString()
                        .replace(",", "-").replace(" ", ""))
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();

        String picture = principal.getClaim("picture");

        ResponseCookie pictureCookie = ResponseCookie.from("picture", picture)
                .httpOnly(false)
                .secure(true)
                .domain(formattedDomain)
                .path("/")
                .sameSite("None")
                .build();

        headers.add(HttpHeaders.SET_COOKIE, authCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessPermissionCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, pictureCookie.toString());

        if (principal.getClaim(roleTokenLocation).toString().contains("Admin")) {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain+"dashboard")).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                    .location(URI.create(frontendDomain)).build();
        }
    }

}
