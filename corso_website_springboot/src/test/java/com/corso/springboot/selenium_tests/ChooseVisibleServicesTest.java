package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChooseVisibleServicesTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void chooseVisibleServicesTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/services");

        sleep(2000);

        $$(".eye-icon").get(0).click();

        sleep(5000);

        String alertTitle = $(".swal-title").text();
        String alertText = $(".swal-text").text();

        assertEquals("Service visibility is updated!", alertTitle);
        assertEquals("The change will be reflected on the main page!", alertText);
        $(".swal-button").click();
    }

}
