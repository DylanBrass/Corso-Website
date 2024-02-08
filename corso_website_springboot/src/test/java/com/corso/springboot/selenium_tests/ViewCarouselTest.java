package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ViewCarouselTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    private static final Logger logger = LoggerFactory.getLogger(ViewCarouselTest.class);

    @Test
    public void viewCarouselTest() {
        logger.info("Test initialized");

        open("http://localhost:3000/");

        // wait for the carousel to load
        sleep(5000);

        // Find the carousel
        SelenideElement carousel = $("div[class='carousel slide']");

        // Assert that the carousel is displayed
        assertTrue(carousel.isDisplayed());

        // Find the carousel indicators (small rectangles showing how many pages there is)
        SelenideElement carouselIndicators = $("ol[class='carousel-indicators']");
        assertTrue(carouselIndicators.isDisplayed());

        // Assert there are 5 carousel indicators
        assertEquals(5, carouselIndicators.findAll("li").size());

        // Find the carousel controls (the arrows on the sides of the carousel)
        SelenideElement carouselControlPrev = $("button[class='carousel-control-prev']");
        SelenideElement carouselControlNext = $("button[class='carousel-control-next']");

        // Find the carousel items (the images in the carousel)
        SelenideElement carouselItems = $("div[class='carousel-inner']");

        // Assert that the carousel items are displayed
        assertTrue(carouselItems.isDisplayed());
        // 4 instead of 5 because one of them is carousel-item active
        assertEquals(4, carouselItems.findAll("div[class='carousel-item']").size());

        // Assert that the first carousel item is active
        assertTrue(carouselItems.find("div[class='carousel-item active']").isDisplayed());

        // Click the right arrow to go to the next carousel item
        carouselControlNext.click();

        // The next <li> element from the carousel indicators becomes active should have class active
        carouselIndicators.find("li[class='active']").shouldHave(Condition.attribute("class", "active"));

        sleep(5000);

        // Click the left arrow to go to the previous carousel item
        carouselControlPrev.click();

        // The previous <li> element from the carousel indicators becomes active should have class active
        carouselIndicators.find("li[class='active']").shouldHave(Condition.attribute("class", "active"));

    }
}
