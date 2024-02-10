package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.remote.tracing.EventAttribute.setValue;

@SpringBootTest
public class ModifyGalleryTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    void modifyGallery() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/carousel/order");

        $$(".mt-2").get(2).click();

        $("#description").setValue("Test");
        sleep(2000);

        File imageFile = new File("C:\\Users\\kehay\\OneDrive\\Bureau\\corsoImage.png");
        $("#image").uploadFile(imageFile);

        $("button[type='submit'][value='Update']").click();

        sleep(10000);

        // Wait for the SweetAlert to appear
        $(".swal-title").should(Condition.exist);

        // Optionally, you can assert the content of the SweetAlert
        String alertTitle = $(".swal-title").text();
        String alertText = $(".swal-text").text();

        assertEquals("Gallery Updated!", alertTitle);
        assertEquals("The gallery has been updated successfully!", alertText);

        // Click on the "OK" button of the SweetAlert
        $(".swal-button").click();

        sleep(2000);

        $$(".mt-2").get(2).click();

        assertEquals("Test", $("#description").getValue());


    }
}
