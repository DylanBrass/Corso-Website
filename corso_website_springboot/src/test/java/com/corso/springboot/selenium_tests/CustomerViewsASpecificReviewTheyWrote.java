package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.ElementsCollection;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@SpringBootTest
public class CustomerViewsASpecificReviewTheyWrote {
    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void CustomerViewsAReviewTheyWrote() {
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
    }

}
