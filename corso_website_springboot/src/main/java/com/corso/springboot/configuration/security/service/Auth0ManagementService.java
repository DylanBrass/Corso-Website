package com.corso.springboot.configuration.security.service;

import com.corso.springboot.configuration.security.exceptions.AddingAdminFailed;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
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

    public ResponseEntity<UserResponse> addAdmin(UserRequest userRequest) throws IOException, InterruptedException {

        String accessToken = null;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("Error getting access token");
            log.error(e.getMessage());
        }

        String body = getFormattedBody(userRequest);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "api/v2/users"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) {
            throwErrorMessage(response);
        }
        log.info("Response: " + response.body());
        List<String> jsonParts = List.of(response.body().split(","));

        for (String part : jsonParts) {
            if (part.contains("user_id")) {
                String userId = "auth0%7C" + part.split(":")[1].replace("\"", "");
                int responseOfAddRole = addAdminRole(userId, accessToken);
                log.info("Response of add role: " + responseOfAddRole);
                if (responseOfAddRole != 204) {
                    deleteUser(userId, accessToken);
                    throw new AddingAdminFailed("Adding role failed", HttpStatus.valueOf(responseOfAddRole));
                }
                else
                    return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        }

        throwErrorMessage(response);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    private static String getFormattedBody(UserRequest userRequest) {
        String username = userRequest.getUsername();
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();

        return """
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
    }

    private void deleteUser(String userId, String accessToken) {
        log.info("Deleting user");
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(issuer + "api/v2/users/" + userId))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            log.info("Response: " + response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    private int addAdminRole(String userId, String accessToken) throws IOException, InterruptedException {
        log.info("Adding admin role to user");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "api/v2/users/" + userId + "/roles"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"roles\":[\"rol_5AZt4iZ6HOc5TMa5\"]}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
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


    public UserInfoResponse getUserInfo(String id) throws  IOException, InterruptedException {
        log.info("Getting user info");
        String accessToken = getAccessToken();

        id = id.replace("|", "%7C");


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "api/v2/users/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response: " + response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            return UserInfoResponse.builder()
                    .username(jsonNode.path("username").asText())
                    .email(jsonNode.path("email").asText())
                    .picture(jsonNode.path("picture").asText())
                    .user_metadata(getUserMetadata(jsonNode))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }


    }


    private HashMap<String, String> getUserMetadata(JsonNode jsonNode) {
        HashMap<String, String> userMetadata = new HashMap<>();
        JsonNode metadataNode = jsonNode.path("user_metadata");

        if (metadataNode.isObject()) {
            metadataNode.fields().forEachRemaining(entry -> userMetadata.put(entry.getKey(), entry.getValue().asText()));
        }

        return userMetadata;
    }

    private String extractToken(String response) {
        return response.split(",")[0].split(":")[1].replace("\"", "");
    }


    public ResponseEntity<Integer> getTotalOfRole(String role_id) throws IOException, InterruptedException {
        log.info("Getting total admins");
        String accessToken = null;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("Error getting access token");
            log.error(e.getMessage());
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(issuer + "api/v2/roles/" + role_id + "/users"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assert response != null;
        log.info("Response: " + response.body());

        int totalUserId = response.body().split("user_id",-1).length - 1;

        return ResponseEntity.ok().body(totalUserId);
        

    }


    public void throwErrorMessage(HttpResponse<String> response) {
        String[] message = response.body().split(",");

        for (String part : message) {
            if (part.contains("message")) {
                String errorMessage = part.split(":")[1].replace("\"", "");
                throw new AddingAdminFailed(errorMessage, HttpStatus.valueOf(response.statusCode()));
            }
        }

        throw new AddingAdminFailed(response.body(), HttpStatus.valueOf(response.statusCode()));
    }
}
