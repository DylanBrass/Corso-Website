package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.Condition;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateAServiceTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the initial URL
    }

    @Test
    public void CreateAServiceSelenideTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/services");
        sleep(2000);

        // add a service
        $(".add-service-icon").click();
        sleep(2000);
        $("#serviceName").setValue("Service for Selenide Test");
        $("#serviceDescription").setValue("Service description for Selenide Test");

        File serviceImageFile = new File("C:\\Users\\kehay\\OneDrive\\Bureau\\testService.jpg");
        $$(".pen-icon").get(0).click();
        $("#serviceImage").uploadFile(serviceImageFile);

        sleep(2000);

        // Upload an image for service icon
        File serviceIconFile = new File("C:\\Users\\kehay\\OneDrive\\Bureau\\ThunderBolt_yguofq.png");
        $$(".pen-icon").get(1).click();
        $("#serviceIcon").uploadFile(serviceIconFile);

        sleep(2000);
        $("input[type='submit'][value='Submit']").click();

        sleep(10000);

        // Wait for the SweetAlert to appear
        $(".swal-title").should(Condition.exist);

        // Optionally, you can assert the content of the SweetAlert
        String alertTitle = $(".swal-title").text();
        String alertText = $(".swal-text").text();

        assertEquals("Service created!", alertTitle);
        assertEquals("Service has been created successfully!", alertText);

        // Click on the "OK" button of the SweetAlert
        $(".swal-button").click();

        sleep(2000);

        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/services");
        sleep(2000);

        // assert that a service with service-name "Test Service for Selenide" exists (last element)
        assertEquals("SERVICE FOR SELENIDE TEST", $$(".service-name").last().text());

        // assert that a service with service-description "Test Description for Selenide" exists
        assertEquals("Service description for Selenide Test", $$(".service-description").last().text());
    }
}
