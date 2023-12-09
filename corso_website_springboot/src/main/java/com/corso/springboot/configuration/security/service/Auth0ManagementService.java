package com.corso.springboot.configuration.security.service;

import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
@Generated
public class Auth0ManagementService {

    @Value("${okta.oauth2.issuer}")
    private String issuer;

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    public ResponseEntity<UserResponse> addAdmin(UserRequest userRequest) throws UnirestException {

        String accessToken = null;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("Error getting access token");
            log.error(e.getMessage());
        }



        String username = userRequest.getUsername();
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        String body = """
        {
         "connection": "Username-Password-Authentication",
         "email": "%s",
         "username": "%s",
         "password": "%s",
         "user_metadata": {
                 "isCreatedAdmin" : "true"
        },
        "picture": "https://res.cloudinary.com/dszhbawv7/image/upload/v1702104434/customcolor_icon_customcolor_background_czpfvq.png",
        "email_verified": false,
        "verify_email": true,
        "app_metadata": {}
        }
        """.formatted(email, username, password);


        com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(issuer + "/api/v2/users")
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + accessToken)
                .header("cache-control", "no-cache")
                .header("Accept", "application/json")
                .body(body)
                .asString();

        log.info("Response: " + response.getBody());
        List<String> jsonParts = List.of(response.getBody().split(","));

        for (String part : jsonParts) {
            if (part.contains("user_id")) {
                String userId = "auth0%7C" + part.split(":")[1].replace("\"", "");
                String responseOfAddRole = addAdminRole(userId, accessToken);
                if (responseOfAddRole != null) {
                    deleteUser(userId, accessToken);
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
                }
                else
                    return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        }

        return ResponseEntity.badRequest().build();

    }

    private void deleteUser(String userId, String accessToken) {
        log.info("Deleting user");
        try {
            com.mashape.unirest.http.HttpResponse<String> response = Unirest.delete(issuer + "/api/v2/users/" + userId)
                    .header("authorization", "Bearer " + accessToken)
                    .asString();
            log.info("Response: " + response.getBody());
        } catch (UnirestException e) {
            log.error(e.getMessage());
        }
    }


    private String addAdminRole(String userId, String accessToken) throws UnirestException {
        log.info("Adding admin role to user");
        com.mashape.unirest.http.HttpResponse<String> response = Unirest.post(issuer + "/api/v2/users/" + userId + "/roles")
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + accessToken)
                .header("cache-control", "no-cache")
                .body("{ \"roles\": [ \"rol_5AZt4iZ6HOc5TMa5\"] }")
                .asString();
        return response.getBody();
    }

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

    private String extractToken(String response) {
        return response.split(",")[0].split(":")[1].replace("\"", "");
    }


}
