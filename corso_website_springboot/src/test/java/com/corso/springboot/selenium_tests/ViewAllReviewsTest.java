package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.files.DownloadActions.click;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ViewAllReviewsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the Corso Main Page
    }
    private static final Logger logger = LoggerFactory.getLogger(ViewAllReviewsTest.class);


        @Test
        public void ViewAllReviewsTest() {
            logger.info("Test initialized");

            $(By.linkText("LOGIN")).click();
            $("#username").setValue("Admin");
            $("[name='action']").click();
            $("#password").setValue("Password!!").pressEnter();
            $(By.linkText("DASHBOARD")).click();
            open("http://localhost:3000/manage/testimonies");

            $(By.name("search")).setValue("John Doe");
            sleep(1000);

            $("button > img").click();
            sleep(1000);

            $(".review-card").shouldHave(text("John Doe"));
            sleep(500);

        }
    }



