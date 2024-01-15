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

        $(By.cssSelector("li:nth-child(2) .MuiSvgIcon-root")).hover().click();
        sleep(1000);

        $("body").hover();

        $(By.cssSelector("li:nth-child(1) .MuiSvgIcon-root")).hover().click();
        sleep(1000);

        $("body").hover();

        $("#f0ea91e3-5cee-43ec-b7c6-93edc810a4a8-button > img").click();
        $(By.linkText("HOME")).click();
        $(By.cssSelector(".col-12:nth-child(4) .testimonies-message")).click();

        sleep(2000);
    }
}