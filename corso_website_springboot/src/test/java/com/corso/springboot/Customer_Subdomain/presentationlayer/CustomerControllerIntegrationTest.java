package com.corso.springboot.Customer_Subdomain.presentationlayer;

import com.corso.springboot.Customer_Subdomain.businesslayer.CustomerService;
import com.corso.springboot.Customer_Subdomain.datalayer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc//(addFilters = false)
class CustomerControllerIntegrationTest {

    private final String BASE_URI_CUSTOMERS = "/api/v1/corso/customers";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        // Any setup if needed
    }


    @Test
    void getCustomerByUserIdWithSimpleCheck() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "?simpleCheck=true")
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerByUserIdWithSimpleCheckNotFound() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS + "?simpleCheck=true")
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|newuser")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerByUserId() throws Exception {
        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Doe"));
    }

    @Test
    void deleteCustomer() throws Exception {

        mockMvc.perform(delete(BASE_URI_CUSTOMERS)
                .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        // Perform a subsequent get request to verify the deletion
        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void createCustomer() throws Exception {


        mockMvc.perform(post(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|newuser")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"phone\": \"4506597338\",\n" +
                                "    \"address\": \"123 Main St\",\n" +
                                "    \"email\": \"email@email.com\",\n" +
                                "    \"city\": \"New York\",\n" +
                                "    \"postalCode\": \"J5R 5J4\",\n" +
                                "    \"apartmentNumber\": \"1\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.postalCode").value("J5R 5J4"))
                .andExpect(jsonPath("$.apartmentNumber").value("1"));

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|newuser")))
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
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|newuser")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"phone\": \"4506597338\",\n" +
                                "    \"address\": \"123 Main St\",\n" +
                                "    \"email\": \"emailemail.com\",\n" +
                                "    \"city\": \"New York\",\n" +
                                "    \"postalCode\": \"J5R 5J4\",\n" +
                                "    \"apartmentNumber\": \"1\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());

    }


    @Test
    void updateCustomer() throws Exception {

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Doe"));

        mockMvc.perform(put(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"phone\": \"4506597338\",\n" +
                                "    \"address\": \"123 Main St\",\n" +
                                "    \"email\": \"email@email.com\",\n" +
                                "    \"city\": \"New York\",\n" +
                                "    \"postalCode\": \"J5R 5J4\",\n" +
                                "    \"apartmentNumber\": \"1\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.postalCode").value("J5R 5J4"))
                .andExpect(jsonPath("$.apartmentNumber").value("1"));

        mockMvc.perform(get(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("4506597338"))
                .andExpect(jsonPath("$.name").value("Alice Doe"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }


    @Test
    void updateAlmostNoInfoForCustomer() throws Exception {
        mockMvc.perform(put(BASE_URI_CUSTOMERS)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin().idToken(i -> i.subject("google|123456789")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"phone\": \"+1234567890\",\n" +
                                "    \"address\": \"123 Main St\",\n" +
                                "    \"email\": \"alice.doe@gmail.com\",\n" +
                                "    \"city\": \"Cityville\",\n" +
                                "    \"postalCode\": \"A1B 2C3\",\n" +
                                "    \"apartmentNumber\": \"Apt 5\",\n" +
                                "   \"name\": \"Alice Doe\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+1234567890"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("Cityville"))
                .andExpect(jsonPath("$.postalCode").value("A1B 2C3"))
                .andExpect(jsonPath("$.apartmentNumber").value("Apt 5"))
                .andExpect(jsonPath("$.name").value("Alice Doe"));


    }
}
