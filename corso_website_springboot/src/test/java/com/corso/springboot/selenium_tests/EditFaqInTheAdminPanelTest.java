package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class EditFaqInTheAdminPanelTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void editFaqTest() {
        $(By.linkText("ENGLISH")).click();
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();

        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/manage/faqs");

        $(".col-md-6:nth-child(1) .edit-specific-classBtn > img").click();

        $("#question").should(appear);
        $("#question").setValue("What is the meaning of life?");
        $("#question").shouldHave(value("What is the meaning of life?"));

        $("#answer").should(appear);
        $("#answer").setValue("42");
        $("#answer").shouldHave(value("42"));

        $(".save-faq-button").click();

        $("button.swal-button--confirm").click();

        sleep(2000);

        $(".faq-question").shouldHave(text("What is the meaning of life?"));
        $(".faq-answer").shouldHave(text("42"));

        sleep(2000);
    }
}
