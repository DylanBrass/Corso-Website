package com.corso.springboot.configuration.security.service;

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

import java.io.IOException;
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


    private final Auth0ManagementService auth0ManagementService;


    public void addRoleToUser(String userId, String accessToken) {
        log.info("Adding role to user");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "/api/v2/users/" + userId + "/roles"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString("{ \"roles\": [\"rol_9AIxt3Pgft1HDi9B\"] }"))
                .build();

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        log.info("Response: " + response.body());
    }

    public void sendVerificationEmail(String userId, String accessToken) {
        log.info("Sending verification email");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "/api/v2/jobs/verification-email"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString("{ \"user_id\": \"" + userId + "\" }"))
                .build();

        log.info("Request: " + request.toString());

        HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        assert response != null;
        log.info("Response: " + response.body());
    }



    public ResponseEntity<Void> handleExternalLogin(OidcUser principal) throws IOException, InterruptedException {
        log.info("Handling Apple Login");

        if (!principal.getClaims().containsKey("https://corso-dev.com/roles")
                || principal.getClaim("https://corso-dev.com/roles").toString().equals("[]")) {
            log.info("No roles found, adding default role");
            String accessToken = auth0ManagementService.getAccessToken();

            String userId = principal.getSubject().replace("|", "%7C");

                addRoleToUser(userId, accessToken);


            return ResponseEntity.status(302)
                    .location(URI.create("http://localhost:3000/external")).build();

        }

        return getVoidResponseEntity(principal);
    }


    public ResponseEntity<Void> getVoidResponseEntity(@AuthenticationPrincipal OidcUser principal) {
        HttpHeaders headers = new HttpHeaders();

        ResponseCookie cookie = ResponseCookie.from("id_token",principal.getIdToken().getTokenValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie authCookie = ResponseCookie.from("isAuthenticated", "true")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();

        ResponseCookie accessPermissionCookie = ResponseCookie.from("accessPermission", principal.getAuthorities().toString()
                        .replace(",","-").replace(" ",""))
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .build();

        String picture = principal.getClaim("picture");

        ResponseCookie pictureCookie = ResponseCookie.from("picture", picture)
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("none")
                .build();


        log.info("Subject: " + principal.getSubject());
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, authCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, accessPermissionCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, pictureCookie.toString());

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers)
                .location(URI.create("http://localhost:3000")).build();
    }

}
