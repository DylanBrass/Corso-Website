package com.corso.springboot.configuration.csrf;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Slf4j
@Generated
public final class SpaCsrfToken extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        log.info("CSRF Token in handle: " + csrfToken.get().getToken());


        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {


        log.info("CSRF Token in resolveCsrfMethod: " + csrfToken.getToken());
        if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
            log.info("CSRF Token in resolveCsrfMethod 1: " + csrfToken.getToken());
            return super.resolveCsrfTokenValue(request, csrfToken);
        }



        log.info("CSRF Token in resolveCsrfMethod 2: " + csrfToken.getToken());
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }

}