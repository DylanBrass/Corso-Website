package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@SpringBootTest
public class ViewAllOrdersTest {
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
        open("http://localhost:3000/orders/all");

        sleep(2000);

        $("#pageSize").selectOption("5");

        sleep(2000);

        $("h1").shouldBe(visible).shouldHave(exactText("ALL ORDERS"));

        $(".orders-tile").shouldBe(visible);

        $(".order-id").shouldBe(visible);

        $("#pageSize").selectOption("1");

        WebElement pageButton = $(".MuiPagination-ul.css-wjh20t-MuiPagination-ul li button", 2);

        $(pageButton).click();

        sleep(2000);

        $("h1").shouldBe(visible).shouldHave(exactText("ALL ORDERS"));

        $(".orders-tile").shouldBe(visible);

        $(".order-id").shouldBe(visible);
    }
}
