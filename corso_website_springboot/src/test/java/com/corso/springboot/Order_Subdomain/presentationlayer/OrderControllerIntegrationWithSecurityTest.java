package com.corso.springboot.Order_Subdomain.presentationlayer;

import com.corso.springboot.Order_Subdomain.businesslayer.OrderService;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderServiceImpl;
import com.corso.springboot.Order_Subdomain.businesslayer.OrderServiceImplUnitTest;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderControllerIntegrationWithSecurityTest {

    private final String BASE_URI_ORDERS = "/api/v1/corso/orders";
    private final String BASE_URI_CUSTOMERS = "/api/v1/corso/customers";

    @MockBean
    private EmailService emailService;

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
                                    "hoursWorked": 10
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
                                    "hoursWorked": 10
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderDescription").value("Test order"))
                .andExpect(jsonPath("$.userId").value("google|123456789"));
    }


    @Test
    public void whenCustomerGetsAllOrders_thenReturnAllOrders() throws Exception {
        //arrange
        Integer expectedNumOrders = 5;

        //act
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "/orders?pageSize=50&offset=0")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedNumOrders));
    }

    @Test
    public void whenCustomerGetsAllOrdersWithNoOrders_thenReturnNotFound() throws Exception {
        //act
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "/orders?pageSize=50&offset=0")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac88")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenCustomerGetsAllOrdersWithPageSize0AndOffset0_thenReturnNotFound() throws Exception {
        Integer expectedNumOrders = 5;

        //act
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "/orders?pageSize=0&offset=0")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedNumOrders));
    }

    @Test
    public void getOrderById_thenReturnOrder() throws Exception {
        //arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f411";
        //act

        mockMvc.perform(get(BASE_URI_ORDERS + "/manage/" + orderId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));


    }






    @Test
    public void getOrderByInvalidId_thenReturnNotFound() throws Exception {
        //arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f412";
        //act

        mockMvc.perform(get(BASE_URI_ORDERS + "/manage/" + orderId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")).authorities(new SimpleGrantedAuthority("Admin")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerCurrentOrders() throws Exception {

        mockMvc.perform(get(BASE_URI_ORDERS + "/current")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }
    @Test
    public void whenOrderWithStatusPendingAndAcceptedWithValidOrderId_thenReturnSuccessful() throws Exception {
        //arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f411";

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(200);

        //act
        mockMvc.perform(patch(BASE_URI_ORDERS + "/manage/acceptOrder/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\": \"Test progress information\"}")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk());
    }
    @Test
    public void whenOrderWithStatusPendingAndAcceptedWithInvalidOrderId_thenReturnNotFound() throws Exception {
        //arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f412";

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class),any())).thenReturn(200);

        //act
        mockMvc.perform(patch(BASE_URI_ORDERS + "/manage/acceptOrder/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\": \"Test progress information\"}")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenOrderWithStatusPendingIsSetToStatusDeclinedWithValidOrderId_thenReturnSuccessful() throws Exception {
        // Arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f411";

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class), any())).thenReturn(200);

        // Act and Assert
        mockMvc.perform(patch(BASE_URI_ORDERS + "/manage/declineOrder/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\": \"Test progress information\", " +
                                "\"reasonForDecline\": \"Test progress information\"}")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenOrderWithStatusPendingIsSetToStatusDeclinedWithInvalidOrderId_thenReturnNotFound() throws Exception {
        // Arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f412";

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class), any())).thenReturn(200);

        // Act and Assert
        mockMvc.perform(patch(BASE_URI_ORDERS + "/manage/declineOrder/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\": \"Test progress information\", " +
                                "\"reasonForDecline\": \"Test progress information\"}")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenOrderWithStatusInProgressIsSetToStatusCancelledWithValidOrderId_thenReturnSuccessful() throws Exception {
        // Arrange
        String orderId = "ac5ca2b4-d53c-4516-8d4b-17665b46f411";

        Mockito.when(emailService.sendEmail(any(String.class), any(String.class), any(String.class), any())).thenReturn(200);

        // Act and Assert
        mockMvc.perform(patch(BASE_URI_ORDERS + "/manage/cancelOrder/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipient\": \"Test progress information\", " +
                                "\"reasonForDecline\": \"Test progress information\"}")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk());
    }

    @Test
    void getCountOfOverdueOrders_ShouldReturnOne() throws Exception {
        // Arrange
        // return one because only one is pending
        Integer expectedCount = 1;

        // Act and Assert
        mockMvc.perform(get(BASE_URI_ORDERS + "/manage/overdue/count")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedCount));
    }

}
