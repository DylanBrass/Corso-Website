package com.corso.springboot.configuration.security.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Generated;
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
public class Auth0Service {

    @Value("${okta.oauth2.issuer}")
    private String issuer;

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    public String getAccessToken() throws IOException, InterruptedException {
        log.info("Getting Access Token");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "oauth/token"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"client_id\":\"" + clientId + "\",\"client_secret\":\"" + clientSecret + "\",\"audience\":\"" + issuer + "api/v2/\",\"grant_type\":\"client_credentials\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("Access Token: " + extractToken(response.body()));
        return extractToken(response.body());
    }

    public void addRoleToUser(String userId, String accessToken) throws UnirestException {
        log.info("Adding role to user");
        com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(issuer + "/api/v2/users/" + userId + "/roles")
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + accessToken)
                .header("cache-control", "no-cache")
                .body("{ \"roles\": [ \"rol_9AIxt3Pgft1HDi9B\"] }")
                .asString();
        log.info("Response: " + response.getBody());
    }

    public void sendVerificationEmail(String userId, String accessToken) {
        log.info("Sending verification email");
        com.mashape.unirest.http.HttpResponse<String> response = null;
        try {
            response = Unirest.post(issuer + "/api/v2/jobs/verification-email")
                    .header("content-type", "application/json")
                    .header("authorization", "Bearer " + accessToken)
                    .header("cache-control", "no-cache")
                    .body("{ \"user_id\": \"" + userId + "\" }")
                    .asString();
            log.info("Response: " + response.getBody());
        } catch (UnirestException e) {
            log.error(e.getMessage());
        }
        assert response != null;
        log.info("Response: " + response.getBody());
    }


    private String extractToken(String response) {
        return response.split(",")[0].split(":")[1].replace("\"", "");
    }

    public ResponseEntity<Void> handleExternalLogin(OidcUser principal) throws IOException, InterruptedException {
        log.info("Handling Apple Login");
        String accessToken = getAccessToken();

        if (!principal.getClaims().containsKey("https://corso-dev.com/roles")
                || principal.getClaim("https://corso-dev.com/roles").toString().equals("[]")) {
            log.info("No roles found, adding default role");

            String userId = principal.getSubject().replace("|", "%7C");

            try {
                addRoleToUser(userId, accessToken);
            } catch (UnirestException e) {
                log.error(e.getMessage());
            }



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
