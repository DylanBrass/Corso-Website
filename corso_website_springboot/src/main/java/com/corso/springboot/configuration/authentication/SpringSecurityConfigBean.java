package com.corso.springboot.configuration.authentication;


import com.corso.springboot.configuration.authentication.models.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfigBean {
//    @Value("${okta.oauth2.issuer}")
//    private String issuer;

//    @Value("${okta.oauth2.client-id}")
//    private String clientId;
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
                .oauth2ResourceServer(jwt -> jwt.jwt(withDefaults()))
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                })
                .csrf(httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/corso/logout"));
                });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(Arrays.toString(new String[]{"http://localhost:3000","http://localhost:8080"}));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
//        config.addAllowedHeader("*");
        config.addAllowedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.error("Authentication error: {}", authException.getMessage());
            log.error("Authentication error: {}", authException.getLocalizedMessage());
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwtToken = authorizationHeader.substring(7); // Assuming "Bearer " prefix
                log.info("JWT Token: {}", jwtToken);
            }

            // Retrieve cookies from the request
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.info("Cookie - Name: {}, Value: {}", cookie.getName(), cookie.getValue());
                }
            }

            final ErrorMessage errorMessage = ErrorMessage.from("Requires authentication");
            final String json = mapper.writeValueAsString(errorMessage);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(json);
            response.flushBuffer();
        };
    }

//    private LogoutHandler logoutHandler() {
//        return (request, response, authentication) -> {
//            try {
//                response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };
//    }
}
