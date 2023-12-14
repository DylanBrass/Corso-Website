package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.visible;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ViewServiceDetailsTest {

    private static final Logger logger = LoggerFactory.getLogger(ViewAllServicesTest.class);

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the initial URL
    }

    @Test
    public void viewAllServicesTest() {
        logger.info("Test initialized");

        sleep(1000);

        // Scroll to the services container
        SelenideElement services = $("div[class='container services-container']");
        services.shouldBe(visible);

        assertTrue(services.isDisplayed());

        sleep(1000);

        // Click the view details link
        SelenideElement viewDetailsLink = $$("a.details-hyperlink").first();
        viewDetailsLink.shouldBe(visible).click();

        sleep(1000);

        SelenideElement servicesDetails = $("div.service-details-container");
        servicesDetails.shouldBe(visible);
        assertTrue(servicesDetails.isDisplayed());

        sleep(1000);

        // Click the back to main page link
        SelenideElement backToMainPageLink = $("a.home-hyperlink");
        backToMainPageLink.shouldBe(visible).click();

        sleep(500);
    }
}
