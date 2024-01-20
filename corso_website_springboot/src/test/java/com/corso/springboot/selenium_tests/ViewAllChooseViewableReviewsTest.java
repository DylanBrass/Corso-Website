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
public class ViewAllChooseViewableReviewsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the Corso Main Page
    }
    private static final Logger logger = LoggerFactory.getLogger(ViewAllChooseViewableReviewsTest.class);


    @Test
    public void ViewAllPinnedReviewsTest() {
        logger.info("Test initialized");

        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/manage/testimonies");

        $(".review-eye").click();
        sleep(5000);

        $("#pageSize").selectOption("5");

        sleep(3000);

        $(By.linkText("HOME")).click();


        SelenideElement pinnedReviews = $(".col-12:nth-child(3)");
        pinnedReviews.scrollIntoView("{behavior: 'smooth', block: 'center'}");

        assertTrue(pinnedReviews.isDisplayed());

        sleep(2000);


    }
}



