package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ViewAllPinnedReviewsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the Corso Main Page
    }
    private static final Logger logger = LoggerFactory.getLogger(ViewAllPinnedReviewsTest.class);


    @Test
    public void ViewAllPinnedReview(){
        logger.info("Test initialized");


        SelenideElement pinnedReviews = $(".col-12:nth-child(2) > .testimonies-info");
        pinnedReviews.scrollIntoView("{behavior: 'smooth', block: 'center'}");

        assertTrue(pinnedReviews.isDisplayed());

        sleep(2000);

    }
    }



