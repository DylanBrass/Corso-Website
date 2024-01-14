package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChooseViewableFaqsTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void chooseViewableFaqsTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/manage/faqs");

        sleep(2000);
        $(".faq-img-0").click();
        sleep(2000);

        $(".faq-img-1").click();
        sleep(2000);

        $(".save-viewable-faqs-button").click();
        sleep(3000);

        String alertTitle = $(".swal-title").text();
        String alertText = $(".swal-text").text();

        assertEquals("Viewable FAQs updated!", alertTitle);
        assertEquals("The main page will display the newly selected viewable FAQs!", alertText);
        $(".swal-button").click();

        // assert that there are 3 faqs with classname "faq-pinned-img"
        assertEquals(3, $$(".faq-pinned-img").size());
    }
}
