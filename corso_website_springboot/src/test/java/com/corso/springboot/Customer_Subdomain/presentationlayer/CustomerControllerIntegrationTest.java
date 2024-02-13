package com.corso.springboot.Customer_Subdomain.presentationlayer;


import com.corso.springboot.Order_Subdomain.datalayer.Order;
import com.corso.springboot.Order_Subdomain.datalayer.OrderTrackingNumber;
import com.corso.springboot.Order_Subdomain.datamapperlayer.OrderRequestMapper;
import com.corso.springboot.Order_Subdomain.presentationlayer.OrderRequestCustomer;
import com.corso.springboot.configuration.security.models.UserInfoResponse;
import com.corso.springboot.configuration.security.service.Auth0ManagementService;
import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.email.datalayer.VerificationToken;
import com.corso.springboot.email.datalayer.VerificationTokenRepository;
import com.corso.springboot.utils.Tools.BasicTools;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    private final String BASE_URI_CUSTOMERS = "/api/v1/corso/customers";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;


    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @MockBean
    private Auth0ManagementService auth0ManagementService;

    @MockBean
    private BasicTools basicTools;

    @Mock
    OrderRequestMapper orderRequestMapper;




    @Test
    void getCustomerByUserIdWithSimpleCheck() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "?simpleCheck=true")
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerByUserIdWithSimpleCheckNotFound() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "?simpleCheck=true")
                        .with(oidcLogin().idToken(i -> i.subject("google|newuser")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerByUserId() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Doe"));
    }

    @Test
    void deleteCustomer() throws Exception {

        mockMvc.perform(delete(BASE_URI_CUSTOMERS)
                .with(oidcLogin().idToken(i -> i.subject("google|123456789")).authorities(new SimpleGrantedAuthority("Customer")))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        // Perform a subsequent get request to verify the deletion
        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void createCustomer() throws Exception {


        mockMvc.perform(post(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> {
                                    i.subject("google|newuser");
                                    i.claim("email", "test@email.com");
                                }
                        ).authorities(new SimpleGrantedAuthority("Customer")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "phone": "4506597338",
                                    "address": "123 Main St",
                                    "email": "email@email.com",
                                    "city": "New York",
                                    "postalCode": "J5R 5J4",
                                    "apartmentNumber": "1"
                                }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.postalCode").value("J5R 5J4"))
                .andExpect(jsonPath("$.apartmentNumber").value("1"));

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(oidcLogin().idToken(i -> i.subject("google|newuser")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.postalCode").value("J5R 5J4"))
                .andExpect(jsonPath("$.apartmentNumber").value("1"));

    }


    @Test
    void postWithInvalidBody() throws Exception {
        mockMvc.perform(post(BASE_URI_CUSTOMERS)
                        .with(oidcLogin().idToken(i -> i.subject("google|newuser")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {    "phone": "4506597338",
                                    "address": "123 Main St",
                                    "email": "emailemail.com",
                                    "city": "New York",
                                    "postalCode": "J5R 5J4",
                                    "apartmentNumber": "1"
                                }"""))
                .andExpect(status().isBadRequest());

    }


    @Test
    void updateCustomer() throws Exception {

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> {
                            i.subject("google|123456789");
                            i.claim("email", "test@email.com");
                        }))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Doe"));

        mockMvc.perform(put(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> {
                            i.subject("google|123456789");
                            i.claim("email", "test@email.com");
                        }).authorities(new SimpleGrantedAuthority("Customer")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "phone": "4506597338",
                                    "address": "123 Main St",
                                    "email": "email@email.com",
                                    "city": "New York",
                                    "postalCode": "J5R 5J4",
                                    "apartmentNumber": "1"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.postalCode").value("J5R 5J4"))
                .andExpect(jsonPath("$.apartmentNumber").value("1"));

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.name").value("Alice Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }


    @Test
    void updateAlmostNoInfoForCustomer() throws Exception {
        mockMvc.perform(put(BASE_URI_CUSTOMERS)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> {
                            i.subject("google|123456789");
                            i.claim("email", "test@email.com");
                        }).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "phone": "+1234567890",
                                    "address": "123 Main St",
                                    "email": "alice.doe@gmail.com",
                                    "city": "Cityville",
                                    "postalCode": "A1B 2C3",
                                    "apartmentNumber": "Apt 5",
                                    "name": "Alice Doe"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("Cityville"))
                .andExpect(jsonPath("$.postalCode").value("A1B 2C3"))
                .andExpect(jsonPath("$.apartmentNumber").value("Apt 5"))
                .andExpect(jsonPath("$.name").value("Alice Doe"));
    }


    @Test
    void getCustomerWithOrders() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "/orders?pageSize=50&offset=0")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getOrderByOrderId() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "/orders/ac5ca2b4-d53c-4516-8d4b-17665b46f411")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingNumber").value("T1YX4S"));
    }

    @Test
    void customerRequestsToCancelOrderRequest_ShouldSucceed() throws Exception {

        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);

        mockMvc.perform(delete(BASE_URI_CUSTOMERS + "/orders/a0119367-4963-4019-b433-ba639fdf6b41")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "reason": "I don't want it anymore"
                                }"""))
                .andExpect(status().isNoContent());
    }


    @Test
    void customerRequestsToCancelOrderWithIn_ProgressStatus_ShouldReturn422() throws Exception {
        mockMvc.perform(delete(BASE_URI_CUSTOMERS + "/orders/ac5ca2b4-d53c-4516-8d4b-17665b46f411")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "reason": "I don't want it anymore"
                                    }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void customerRequestsToCancelOrderThatDoesNotExist_ShouldReturn404() throws Exception {
        mockMvc.perform(delete(BASE_URI_CUSTOMERS + "/orders/does-not-exist")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "reason": "I don't want it anymore"
                                }"""))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOrderRequest_ShouldSucceed() throws Exception {

        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);


        mockMvc.perform(patch(BASE_URI_CUSTOMERS + "/orders/a0119367-4963-4019-b433-ba639fdf6b41")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerPhone": "4506597338",
                                    "customerAddress": "123 Main St",
                                    "customerCity": "New York",
                                    "customerPostalCode": "J5R 5J4",
                                    "customerApartmentNumber": "1"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerPhone").value("4506597338"))
                .andExpect(jsonPath("$.customerAddress").value("123 Main St"))
                .andExpect(jsonPath("$.customerCity").value("New York"))
                .andExpect(jsonPath("$.customerPostalCode").value("J5R 5J4"));

    }

    @Test
    void updateOrderRequestWithNotPendingOrder_ShouldReturn422() throws Exception {
        mockMvc.perform(patch(BASE_URI_CUSTOMERS + "/orders/ac5ca2b4-d53c-4516-8d4b-17665b46f411")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "customerPhone": "4506597338",
                                    "customerAddress": "123 Main St",
                                    "customerCity": "New York",
                                    "customerPostalCode": "J5R 5J4",
                                    "customerApartmentNumber": "1"
                                }"""))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Order cannot be updated, it is not in pending status, meaning it was accepted or cancelled already!"));

    }

    @Test
    void updateReviewNotPinned_ShouldSucceed() throws Exception {
        //arrange
        String reviewId = "8987c71a-861b-496c-a927-0683816cd490";


        //act
        mockMvc.perform(patch(BASE_URI_CUSTOMERS + "/reviews/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89")).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "This is a test message",
                                    "reviewRating": 5,
                                    "customerFullName": "Alice Doe"
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("This is a test message"))
                .andExpect(jsonPath("$.reviewRating").value(5));
    }


    @Test
    void updateReviewWithRatingGreaterThan5_ShouldReturn400() throws Exception {
        //arrange
        String reviewId = "8987c71a-861b-496c-a927-0683816cd490";

        //act

        mockMvc.perform(patch(BASE_URI_CUSTOMERS + "/reviews/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "This is a test message",
                                    "reviewRating": 6,
                                    "customerFullName": "Alice Doe"
                                }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Review rating must be between 1 and 5"));

    }

    @Test
    void updateReviewWithRatingLessThen1_ShouldGive400() throws Exception {
        //arrange
        String reviewId = "8987c71a-861b-496c-a927-0683816cd490";

        //act

        mockMvc.perform(patch(BASE_URI_CUSTOMERS + "/reviews/" + reviewId)
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|65702e81e9661e14ab3aac89"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "This is a test message",
                                    "reviewRating": 0
                                                                    }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Review rating must be between 1 and 5"));



    }


    @Test
    void verifyCustomer_ShouldSucceed() throws Exception {

        verificationTokenRepository.deleteAll();


        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);


        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")

                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|6543210987"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isNoContent());


    }

    @Test
    void verifyNotFoundCustomer_ShouldReturn404() throws Exception {


        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")

                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|wrong"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isNotFound());
    }


    @Test
    void verifyAlreadyVerifiedCustomer_ShouldReturn400() throws Exception {

        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")

                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    void verifyUserWithExpiredAlreadyExistingToken_ShouldSucceed() throws Exception {

        VerificationToken verificationToken = new VerificationToken("test@email.com",
                "123456789");

        verificationTokenRepository.save(verificationToken);


        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);


        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|6543210987"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Email already sent, please check your inbox, otherwise try again in 15 minutes"));



    }


    @Test
    void verifyUserWithExpiredAlreadyExistingTokenSameEmail_ShouldSucceed() throws Exception {

        verificationTokenRepository.deleteAll();

        VerificationToken verificationToken = new VerificationToken("auth0|6543210987",
                "sophia.lee@outlook.com");

        verificationTokenRepository.save(verificationToken);


        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);


        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|6543210987"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Email already sent, please check your inbox, otherwise try again in 15 minutes"));



    }


    @Test
    void verifyUserWithExpiredAlreadyExistingTokenDifferentEmail_ShouldSucceed() throws Exception {

        verificationTokenRepository.deleteAll();

        VerificationToken verificationToken = new VerificationToken("auth0|6543210987",
                "test.lee@outlook.com");

        verificationTokenRepository.save(verificationToken);


        Mockito.when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);


        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/request/verification")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("auth0|6543210987"))
                                .authorities(new SimpleGrantedAuthority("Customer"))))
                .andExpect(status().isNoContent());



    }

    @Test
    public void whenCreatingOrderWithValidValues_thenReturnCreatedOrder() throws Exception {
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email("test@email.com")
                .username("external_client")
                .picture("https://external_client.com/picture.jpg")
                .build();

        Order order = Order.builder()
                .orderTrackingNumber(new OrderTrackingNumber())
                .orderDescription("Test order")
                .customerFullName("Test User")
                .customerPhone("1234567890")
                .customerAddress("Test Address")
                .customerCity("Test City")
                .customerPostalCode("A1A 1A1")
                .customerApartmentNumber("1")
                .dueDate("2022-12-12")
                .build();

        when(orderRequestMapper.requestModelToOrderCustomer(any(OrderRequestCustomer.class))).thenReturn(order);

        when(auth0ManagementService.getUserInfo("google|123456789")).thenReturn(userInfoResponse);

        when(emailService.sendEmail(anyString(),anyString(),anyString(),anyMap())).thenReturn(200);

        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/orders/request")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> i.subject("google|123456789"))
                                .authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "serviceId": "982577e6-1909-46b8-8583-e08c9daa4e9b",
                                    "orderDescription": "Test order",
                                    "customerFullName": "Test User",
                                    "customerPhone": "1234567890",
                                    "customerAddress": "Test Address",
                                    "customerCity": "Test City",
                                    "customerPostalCode": "A1A 1A1",
                                    "customerApartmentNumber": "1",
                                    "dueDate": "2022-12-12"
                                    }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreatingOrderWithInvalidValues_thenReturnBadRequest() throws Exception {
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .email("test@email.com")
                .username("external_client")
                .picture("https://external_client.com/picture.jpg")
                .build();

        when(auth0ManagementService.getUserInfo("google|123456789")).thenReturn(userInfoResponse);

        when(emailService.sendEmail(anyString(), anyString(), anyString(), anyMap())).thenReturn(200);

        mockMvc.perform(post(BASE_URI_CUSTOMERS + "/orders/request")
                        .with(csrf())
                        .with(oidcLogin().idToken(i -> {
                            i.subject("google|123456789");
                            i.claim("email", "email@email.com");
                        }).authorities(new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "serviceId": null,
                                    "orderDescription": null,
                                    "customerFullName": null,
                                    "customerPhone": null,
                                    "customerAddress": null,
                                    "customerCity": null,
                                    "customerPostalCode": null,
                                    "customerApartmentNumber": "1"
                                    }
                                """))
                .andExpect(status().isBadRequest());
    }

}