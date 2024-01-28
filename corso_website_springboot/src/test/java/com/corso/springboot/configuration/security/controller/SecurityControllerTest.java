package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.email.businessLayer.EmailService;
import com.corso.springboot.email.datalayer.VerificationToken;
import com.corso.springboot.email.datalayer.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SecurityControllerTest {

    private final String BASE_URI_SECURITY = "/api/v1/corso/security";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;


    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    void verifyUser_ShouldSucceed() throws Exception {

        verificationTokenRepository.deleteAll();

        VerificationToken verificationToken = new VerificationToken("auth0|6543210987", "sophia.lee@outlook.com");

        verificationTokenRepository.save(verificationToken);


        mockMvc.perform(post(BASE_URI_SECURITY + "/verify")
                        .with(csrf())
                .content("{\"token\":\"" + verificationToken.getToken() + "\"," +
                        "\"email\": \"" + verificationToken.getEmail() + "\"}")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void verifyUser_ShouldFail() throws Exception {

        verificationTokenRepository.deleteAll();


        mockMvc.perform(post(BASE_URI_SECURITY + "/verify")
                        .with(csrf())
                        .content("{\"token\":\"" + "verificationToken.getToken()"+ "\"," +
                                "\"email\": \"" + "verificationToken.getEmail()" + "\"}")
                        .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void verifyUserWithInvalidToken_ShouldFail() throws Exception {

        verificationTokenRepository.deleteAll();

        VerificationToken verificationToken = new VerificationToken("auth0|6543210987", "sophia.lee@outlook.com");

        verificationToken.setExpiryDate(new Date());

        verificationTokenRepository.save(verificationToken);


        mockMvc.perform(post(BASE_URI_SECURITY + "/verify")
                        .with(csrf())
                        .content("{\"token\":\"" + verificationToken.getToken() + "\"," +
                                "\"email\": \"" + verificationToken.getEmail() + "\"}")
                        .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Token expired"));

        }

}