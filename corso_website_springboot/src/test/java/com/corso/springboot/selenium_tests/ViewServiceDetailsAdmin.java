package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ViewServiceDetailsAdmin {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void viewServiceDetailsAdmin() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();


        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/services");
        sleep(2000);
        $(".service-tile").click();
        sleep(2000);

        assert $(".container").isDisplayed();
        SelenideElement serviceH1 = $("h1");
        serviceH1.shouldBe(visible);

        SelenideElement serviceImage = $("div.view-service-admin-container-content-image");
        serviceImage.shouldBe(visible);

    }
}
