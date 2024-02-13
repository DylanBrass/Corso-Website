package com.corso.springboot.configuration.security.controller;

import com.corso.springboot.configuration.cache.CacheTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://corsoelectriqueinc.tech/"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/corso/server")
public class ServerController {

    @Value("${frontend.url}")
    private String frontendDomain;

    private final CacheTools cacheTools;
    
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES)
    public void evictAllCaches() {
        log.info("Evicting all caches");
        cacheTools.evictAllCaches();
    }

}
