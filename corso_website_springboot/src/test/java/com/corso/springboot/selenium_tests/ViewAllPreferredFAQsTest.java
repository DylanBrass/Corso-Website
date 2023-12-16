package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ViewAllPreferredFAQsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    private static final Logger logger = LoggerFactory.getLogger(ViewAllPreferredFAQsTest.class);

    @Test
    public void viewAllPreferredFaqTest() {
        logger.info("Test initialized");

        //open the main CORSO page
        open("http://localhost:3000/");

        SelenideElement preferredFAQS = $("div[class='faq-main-box']");
        preferredFAQS.scrollIntoView("{behavior: 'smooth', block: 'center'}");

        assertTrue(preferredFAQS.isDisplayed());

        sleep(2000);

    }

}
