package com.corso.springboot.configuration.authentication.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Value;
import org.apache.http.HttpHeaders;

import java.io.IOException;
import jakarta.servlet.Filter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseHeadersFilter implements Filter {
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain
    ) throws IOException, ServletException {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setIntHeader("X-XSS-Protection", 0);
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpResponse.setHeader("X-Frame-Options", "deny");
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; frame-ancestors 'none';");
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
        httpResponse.setHeader(HttpHeaders.PRAGMA, "no-cache");
        httpResponse.setIntHeader(HttpHeaders.EXPIRES, 0);

        chain.doFilter(request, response);
    }

}
