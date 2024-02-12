package com.corso.springboot.configuration.security.service;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.configuration.security.exceptions.AddingAdminFailed;
import com.corso.springboot.configuration.security.exceptions.Auth0Error;
import com.corso.springboot.configuration.security.models.Auth0GetUsersResponse;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.models.UserRequest;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.utils.exceptions.InvalidRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Generated
public class Auth0ManagementService {

    @Value("${okta.oauth2.issuer}")
    private String issuer;

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    private final CustomerService customerService;

    private final EmailService emailService;
    private final String ADMIN_ROLE_ID = "rol_sDxhuMYKzxiG7Sw8";

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
                } else
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
                        "{\"roles\":[\""+ADMIN_ROLE_ID+"\"]}"))
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


    public UserInfoResponse getUserInfo(String id) throws IOException, InterruptedException {
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
                throw new Auth0Error(response.body(), HttpStatus.valueOf(response.statusCode()));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.path("user_id").asText().isEmpty()) {
                throw new Auth0Error("User not found", HttpStatus.NOT_FOUND);
            }

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
        String accessToken;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("Error getting access token");
            log.error(e.getMessage());
            throw new InvalidRequestException("Error getting access token");
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

        int totalUserId = response.body().split("user_id", -1).length - 1;

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


    public ResponseEntity<List<Auth0GetUsersResponse>> getMatchingUsers(Map<String, String> requestParams) {

        List<Auth0GetUsersResponse> users = new ArrayList<>();


        customerService.getCustomerByQueryParams(requestParams).forEach(customer -> {
            users.add(Auth0GetUsersResponse.builder()
                    .user_id(customer.getUserId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .username(customer.getName())
                    .address(customer.getAddress())
                    .apartmentNumber(customer.getApartmentNumber())
                    .city(customer.getCity())
                    .postalCode(customer.getPostalCode())
                    .phone(customer.getPhone())
                    .build());
        });

        try {
            String accessToken = getAccessToken();

            String url = issuer + "api/v2/users?q=";


            if (requestParams.containsKey("username")) {
                url += "username%3A" + requestParams.get("username") + "*";
            }

            if (requestParams.containsKey("email")) {
                url += "email%3A" + requestParams.get("email") + "*";
            }

            if (requestParams.containsKey("name")) {
                url += "name%3A" + requestParams.get("name") + "*";
            }

            url = url
                    .replace("\"", "%22")
                    .replace("*", "%2A")
                    .replace(" ", "%20")
                    .replace("|", "%7C");

            log.info("URL: " + url);

            HttpResponse<String> response = sendHttpRequest(accessToken, url);

            if (response.statusCode() != 200) {
                throw new Auth0Error("Error getting users", HttpStatus.valueOf(response.statusCode()));
            }

            new ObjectMapper().readTree(response.body()).forEach(node -> {
                log.info("Node: " + node.toString());
                 if(node.path("user_id").asText().isEmpty())
                    return;

                if (node.path("user_metadata").path("isCreatedAdmin").asText().equals("true"))
                    return;

                
                users.add(Auth0GetUsersResponse.builder()
                        .user_id(node.path("user_id").asText())
                        .name(node.path("name").asText())
                        .email(node.path("email").asText())
                        .username(node.path("username").asText())
                        .build());
            });

            log.info("Users: " + users);

            HashMap<String, Auth0GetUsersResponse> map = new HashMap<>();

            users.forEach(user -> {
                if (!map.containsKey(user.getUser_id())) {
                    map.put(user.getUser_id(), user);
                }
            });

            return ResponseEntity.ok().body(map.values().stream().toList());


        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

    }


    public ResponseEntity<Integer> getStatsLast30Days() {
        try {
            String accessToken = getAccessToken();
            String url = issuer + "api/v2/stats/active-users";

            HttpResponse<String> response = sendHttpRequest(accessToken, url);


            log.info("Response number : " + response.body());

            Integer total = Integer.parseInt(response.body());


            return ResponseEntity.ok().body(total);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    public List<UserInfoResponse> getAllAdmins() {
        try {
            String accessToken = getAccessToken();
            String url = issuer + "api/v2/roles/"+ADMIN_ROLE_ID+"/users";

            HttpResponse<String> response = sendHttpRequest(accessToken, url);

            List<UserInfoResponse> admins = new ArrayList<>();

            new ObjectMapper().readTree(response.body()).forEach(node -> {
                try {
                    admins.add(getUserInfo(node.path("user_id").asText()));
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            return admins;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> sendHttpRequest(String accessToken, String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assert response != null;

        return response;
    }




}
