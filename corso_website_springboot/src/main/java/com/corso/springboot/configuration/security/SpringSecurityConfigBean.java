package com.corso.springboot.configuration.security;


import com.corso.springboot.configuration.csrf.SpaCsrfToken;
import com.corso.springboot.configuration.filters.CsrfCustomFilter;
import com.corso.springboot.configuration.security.models.ErrorMessage;
import com.corso.springboot.utils.Tools.BasicTools;
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
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    @Value("${frontend.url}")
    private String frontendDomain;

    @Value("${backend.url}")
    private String backendDomain;

    private final BasicTools basicTools;

    private final ObjectMapper mapper;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/corso/auth0/manage/**")).hasAuthority("Admin")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/corso/server/status")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/corso/security/verify")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/corso/email/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/galleries/carousel")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/services")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/services/{serviceId}")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/faqs")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/faqs/preferred")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/reviews/pinned")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/corso/auth0/manage/users")).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()))
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage("/login/oauth2/code/okta")
                        .defaultSuccessUrl(backendDomain + "api/v1/corso/security/redirect", true)
                        .permitAll())
                .oauth2ResourceServer(jwt -> jwt.jwt(withDefaults()))
                .logout(logout -> {
                    logout.logoutUrl("/api/v1/corso/logout")
                            .addLogoutHandler(logoutHandler())
                            .logoutSuccessHandler((request, response, authentication) -> {

                                if (request.getCookies() != null) {

                                    Arrays.stream(request.getCookies()).toList().forEach(cookie -> {
                                        log.info(cookie.getName());
                                        Cookie newCookie = new Cookie(cookie.getName(), "");
                                        newCookie.setMaxAge(0);
                                        newCookie.setPath("/");
                                        newCookie.setDomain(frontendDomain.replace("https://", "").replace("http://", "")
                                                .split(":")[0].replace("/", ""));
                                        response.addCookie(newCookie);

                                    });
                                }
                                response.setStatus(HttpStatus.OK.value());
                            });
                })
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                        .csrfTokenRequestHandler(new SpaCsrfToken())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/v1/corso/logout", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/api/v1/corso/security/redirect", HttpMethod.GET.toString())
                        )
                )
                .cors(httpSecurityCorsConfigurer -> {
                    final var cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of(frontendDomain));
                    cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE, HEAD, PATCH"));
                    cors.setAllowedHeaders(Arrays.asList("authorization", "content-type", "xsrf-token"));
                    cors.setExposedHeaders(List.of("xsrf-token"));
                    cors.setAllowCredentials(true);
                    cors.setMaxAge(3600L);
                })
                .addFilterAfter(new CsrfCustomFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            final String json;
            final ErrorMessage errorMessage;

            if (request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals("isAuthenticated"))) {

                errorMessage = ErrorMessage.from("Your session has expired.");

            } else {
                errorMessage = ErrorMessage.from(authException.getMessage());
            }

            json = mapper.writeValueAsString(errorMessage);

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

            if (request.getCookies() != null) {

                List<Cookie> cookies = List.of(request.getCookies());


                cookies.forEach(cookie -> {
                    Cookie newCookie = new Cookie(cookie.getName(), "");
                    newCookie.setMaxAge(0);
                    newCookie.setPath("/");
                    newCookie.setDomain(basicTools.getFormattedDomain());
                    response.addCookie(newCookie);
                });
            }

            boolean isSignup = Boolean.parseBoolean(request.getParameter("isLogoutSignUp"));
            boolean isVerify = Boolean.parseBoolean(request.getParameter("isLogoutVerify"));
            boolean isExternal = Boolean.parseBoolean(request.getParameter("isLogoutExternal"));

            try {

                if (isSignup)
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + backendDomain + "oauth2/authorization/okta");
                else if (isVerify)
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain + "verify");
                else if (isExternal)
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain + "external");
                else
                    response.sendRedirect(issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + frontendDomain);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }


    public CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
        cookieCsrfTokenRepository.setCookieCustomizer(cookie -> {
            cookie.domain(basicTools.getFormattedDomain());
            cookie.httpOnly(false);
            cookie.path("/");
            cookie.secure(false);

        });


        return cookieCsrfTokenRepository;
    }


}


