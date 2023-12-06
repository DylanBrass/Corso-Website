package com.corso.springboot.configuration.security;


import com.corso.springboot.configuration.csrf.SpaCsrfToken;
import com.corso.springboot.configuration.filters.CsrfCustomFilter;
import com.corso.springboot.configuration.security.models.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Generated
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfigBean {
    @Value("${okta.oauth2.issuer}")
    private String issuer;

    @Value("${okta.oauth2.client-id}")
    private String clientId;
    private final String frontendDomain = "http://localhost:3000";

    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/corso/logout")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/galleries/carousel")).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(authenticationEntryPoint());
                })
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.loginPage("/login/oauth2/code/okta")
                            .defaultSuccessUrl("http://localhost:8080/api/v1/corso/security/redirect", true)
                            .permitAll();
                })
                .oauth2ResourceServer(jwt -> jwt.jwt(withDefaults()))
                .logout(logout -> {

                    logout
                            .logoutUrl("/api/v1/corso/logout")
                            .addLogoutHandler(logoutHandler())
                            .logoutSuccessHandler((request, response, authentication) -> {

                                Arrays.stream(request.getCookies()).toList().forEach(cookie -> {
                                    if (!cookie.getName().equals("JSESSIONID")) {
                                       Cookie newCookie = new Cookie(cookie.getName(), "");
                                        newCookie.setMaxAge(0);
                                        newCookie.setPath("/");
                                        response.addCookie(newCookie);
                                    }
                                });
                                response.setStatus(HttpStatus.OK.value());
                            });
                })
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfToken())
                )
                .cors().and()
                .addFilterAfter(new CsrfCustomFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            final ErrorMessage errorMessage = ErrorMessage.from("Requires security");
            final String json = mapper.writeValueAsString(errorMessage);

            log.error("Error: {}", json);
            log.error("Error: {}", authException.getMessage());
            log.error("Error: {}", authException.getLocalizedMessage());


            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(json);
            response.flushBuffer();
        };
    }



    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {

            try {
                response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}


