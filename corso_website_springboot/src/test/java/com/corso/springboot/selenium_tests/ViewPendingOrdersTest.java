package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@SpringBootTest
public class ViewPendingOrdersTest {
    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the initial URL
    }

    @Test
    public void viewPendingOrdersTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/orders/pending");

        sleep(2000);

        $("#pageSize").selectOption("5");

        sleep(2000);

        $("h1").shouldBe(visible).shouldHave(exactText("CLIENT REQUESTS"));

        $(".orders-tile").shouldBe(visible);

        $(".order-id").shouldBe(visible);

        $("#pageSize").selectOption("1");

        sleep(2000);

        $("h1").shouldBe(visible).shouldHave(exactText("CLIENT REQUESTS"));

        $(".orders-tile").shouldBe(visible);

        $(".order-id").shouldBe(visible);
    }
}
