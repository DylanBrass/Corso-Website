package com.corso.springboot;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@Slf4j
@Generated
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class CorsoWebsiteSpringbootApplication {


    public static void main(String[] args) {

        SpringApplication.run(CorsoWebsiteSpringbootApplication.class, args);
    }
    

}
