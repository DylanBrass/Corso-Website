package com.corso.springboot.configuration.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheTools {
    private final CacheManager cacheManager;

    public void evictAllCaches() {

        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(cacheName -> {
                            log.info("Evicting cache: {}", cacheName);
                            Objects.requireNonNull(cacheManager.getCache(cacheName))
                                    .clear();
                        }
                );

    }
}
