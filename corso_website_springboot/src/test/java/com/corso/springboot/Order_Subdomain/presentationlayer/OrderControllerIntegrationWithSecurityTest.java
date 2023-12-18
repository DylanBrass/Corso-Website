package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.models.UserResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc//(addFilters = false)
public class OrderControllerIntegrationWithSecurityTest {

    private final String BASE_URI_ORDERS = "/api/v1/corso/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Auth0ManagementService auth0ManagementService;

    @Test
    public void whenCreatingOrderWithValidValues_thenReturnCreatedOrder() throws Exception {

        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email("test@email.com")
                .username("external_client")
                .picture("https://external_client.com/picture.jpg")
                .build();


        when(auth0ManagementService.getUserInfo("google|123456789")).thenReturn(userInfoResponse);

        //act
        mockMvc.perform(post(BASE_URI_ORDERS + "/external")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")).authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "serviceId": "982577e6-1909-46b8-8583-e08c9daa4e9b",
                                    "orderDescription": "Test order",
                                    "customerFullName": "Test User",
                                    "customerEmail": "test@email.com",
                                    "customerPhone": "1234567890",
                                    "customerAddress": "Test Address",
                                    "customerCity": "Test City",
                                    "customerPostalCode": "A1A 1A1",
                                    "customerApartmentNumber": "1",
                                    "estimatedDuration": 12,
                                    "daysLeft": 10
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderDescription").value("Test order"))
                .andExpect(jsonPath("$.userId").value("external_client"));

    }



    @Test
    public void whenCreatingOrderWithValidValuesAndUser_thenReturnCreatedOrder() throws Exception {

        //act
        mockMvc.perform(post(BASE_URI_ORDERS + "/external")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")).authorities(new SimpleGrantedAuthority("Admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "google|123456789",
                                    "serviceId": "982577e6-1909-46b8-8583-e08c9daa4e9b",
                                    "orderDescription": "Test order",
                                    "customerFullName": "Test User",
                                    "customerEmail": "test@email.com",
                                    "customerPhone": "1234567890",
                                    "customerAddress": "Test Address",
                                    "customerCity": "Test City",
                                    "customerPostalCode": "A1A 1A1",
                                    "customerApartmentNumber": "1",
                                    "estimatedDuration": 12,
                                    "daysLeft": 10
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderDescription").value("Test order"))
                .andExpect(jsonPath("$.userId").value("google|123456789"));

    }
}
