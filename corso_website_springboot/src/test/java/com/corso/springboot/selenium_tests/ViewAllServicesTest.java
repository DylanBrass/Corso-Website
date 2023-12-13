package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ViewAllServicesTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the initial URL
    }

    private static final Logger logger = LoggerFactory.getLogger(ViewAllServicesTest.class);

    @Test
    public void viewAllServicesTest() {
        logger.info("Test initialized");

        sleep(2000);

        SelenideElement services = $("div[class='container services-container']");
        services.scrollIntoView("{behavior: 'smooth', block: 'center'}");

        assertTrue(services.isDisplayed());

        sleep(2000);
    }

}
