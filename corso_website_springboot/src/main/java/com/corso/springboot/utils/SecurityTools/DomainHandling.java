package com.corso.springboot.utils.SecurityTools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainHandling {

    @Value("${frontend.url}")
    private String frontendDomain;

    public String getFormattedDomain() {
        String url = frontendDomain.replace("https://", "").replace("http://", "")
                .split(":")[0].replace("/", "");

        log.debug("Formatted domain: " + url);

        return url;
    }

}

