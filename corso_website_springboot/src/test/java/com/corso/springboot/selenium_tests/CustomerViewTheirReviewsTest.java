package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.ElementsCollection;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class CustomerViewTheirReviewsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void CustomerViewTheirReviewsTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("dylan.brassard@outlook.com");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();

        sleep(2000);

        $(By.id("profile-image")).click();

        sleep(2000);

        $(byText("View Your Reviews")).click();

        // Assertions for the first review
        $(".customer-review-tile", 0).shouldHave(
                text("Rating: # 1"),
                text("Date: 2024-01-04"),
                text("Nice!")
        );

        ElementsCollection reviewTiles = $$(".customer-review-tile");

        ElementsCollection firstReviewStars = reviewTiles.get(0).findAll(".review-rating img");

        firstReviewStars.shouldHave(size(5));

        ElementsCollection secondReviewStars = reviewTiles.get(1).findAll(".review-rating img");

        // Assertions for the second review
        $(".customer-review-tile", 1).shouldHave(
                text("Rating: # 2"),
                text("Date: 2023-06-15"),
                text("Noticed a significant improvement in website traffic after SEO optimization. Thank you!")
        );
        secondReviewStars.shouldHave(size(5));
    }
}
