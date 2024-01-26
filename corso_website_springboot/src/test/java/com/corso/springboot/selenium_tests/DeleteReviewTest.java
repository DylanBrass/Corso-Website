package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeleteReviewTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void DeleteReviewTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("karinaevang@hotmail.com");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();

        sleep(2000);

        $(By.id("profile-image")).click();

        sleep(2000);

        $(byText("View Your Reviews")).click();

        // Assertions for the first review
        $(".customer-review-tile", 0).shouldHave(
                text("Rating: # 1")
        );

        //click on the tile
        $(".customer-review-tile", 0).click();

        $("h1").shouldHave(text("Your Review"));
        $("p").shouldHave(text("Your review is pinned to the landing page"));
        sleep(2000);

        // click on image with alt text "trash"
        $(By.cssSelector("img[alt='trash']")).click();
        sleep(7000);

        String alertTitle = $(".swal-title").text();
        String alertText = $(".swal-text").text();

        assertEquals("Review deleted successfully!", alertTitle);
        assertEquals("Your review has been successfully deleted!", alertText);
        $(".swal-button").click();

    }
}
