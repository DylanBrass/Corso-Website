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
public class AdminDeclinesRequestTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void viewAllOrdersTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/orders/pending");

        sleep(2000);

        $("h1").shouldBe(visible).shouldHave(exactText("CLIENT REQUESTS"));

        $(".orders-tile").shouldBe(visible);
        $(".orders-tile").click();

        sleep(2000);

        $(".decline-button").shouldBe(visible);
        $(".decline-button").click();
        sleep(1000);

        $(".view-specific-order-admin-textarea").setValue("test");
        $(".decline-popup-button").click();
        sleep(1000);

        $(".swal-title").shouldBe(visible).shouldHave(exactText("Declined!"));
        $(".swal-button--confirm").click();


    }
}
