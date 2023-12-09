package com.corso.springboot.configuration.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Generated
public final class CsrfCustomFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        log.info("CSRF Token: " + csrfToken.getToken());


        if (isInvalidCsrfToken(csrfToken, request)) {
            // Handle the case where the CSRF token is invalid (e.g., return an error response)
            log.warn("Invalid CSRF token detected!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isInvalidCsrfToken(CsrfToken csrfToken, HttpServletRequest request) {
        if (csrfToken == null) {
            log.warn("CSRF token is null!");
            return true;
        }

        if (csrfToken.getToken().isEmpty()) {
            log.warn("CSRF token is empty!");
            return true;
        }


        if(csrfToken.getToken().length() < 36){
            log.warn("CSRF token is less than 36 characters!");
            return true;
        }



        return false;
    }
}