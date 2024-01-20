package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class DeleteSpecificFAQTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/"); // Open the Corso Main Page
    }
    private static final Logger logger = LoggerFactory.getLogger(DeleteSpecificFAQTest.class);


    /*

    HAVE TO DO IT AFTER I DO MY ADD A FAQ STORY, OTHERWISE IT DOESNT WORK


    @Test
    public void viewAllFaqByIdTest() {
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/manage/faqs");

        $(".col-12:nth-child(1)").click();
        sleep(2000);
        $(".col-md-6:nth-child(1) .edit-specific-classBtn > img").click();
        sleep(2000);

    }

     */

}
