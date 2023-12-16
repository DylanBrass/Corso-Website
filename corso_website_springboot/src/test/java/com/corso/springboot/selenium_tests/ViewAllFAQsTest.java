package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.Condition;
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
public class ViewAllFAQsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    private static final Logger logger = LoggerFactory.getLogger(ViewAllFAQsTest.class);

    @Test
    public void viewAllFaqTest() {
        logger.info("Test initialized");

        //open the main CORSO page
        open("http://localhost:3000/");

        //slows down the test
        sleep(2000);

        //This clicks on the FAQ link in the navbar
        $(By.linkText("FAQ")).click();

        sleep(2000);

        // all the FAQ tiles on the page
        $("div.tile-box");

        // clicks the + button in the faq tile
        $("div.tile-box:nth-of-type(1) .faq-btn").click();

        sleep(2000);



        //shows the answer to the FAQ
        assertTrue($("div.tile-box:nth-of-type(1) .answer-box").isDisplayed());
        sleep(2000);

        // Click the - button in the FAQ tile to hide the answer
        $("div.tile-box:nth-of-type(1) .faq-btn").click();
        sleep(2000);

        // Make sure it hid the answer
        assertFalse($("div.tile-box:nth-of-type(1) .answer-box").isDisplayed());
        sleep(2000);


    }

}
