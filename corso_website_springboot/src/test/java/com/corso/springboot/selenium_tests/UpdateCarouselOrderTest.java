package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UpdateCarouselOrderTest {

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        open("http://localhost:3000/");
    }

    @Test
    public void UpdateCarouselOrderTest() {
        // Your existing code
        $(By.linkText("LOGIN")).click();
        $("#username").setValue("Admin");
        $("[name='action']").click();
        $("#password").setValue("Password!!").pressEnter();

        // Navigate to the gallery
        $(By.linkText("DASHBOARD")).click();
        open("http://localhost:3000/carousel/order");
        sleep(4000);

        Actions actions = new Actions(getWebDriver());

        // print the image src of the first element
        String srcBeforeDrag = $$(".gallery-item").first().$("img").getAttribute("src");
        System.out.println("Before dragging: " + srcBeforeDrag);

        drag_and_drop_to($$(".gallery-item").first(), $$(".gallery-item").get(2));

        // Wait for the target element to be visible and clickable before releasing
        $$(".gallery-item").get(2).shouldBe(visible, Condition.enabled);

        sleep(2000);

        $(".save-order-button").click();

        // Wait for the SweetAlert to appear
        $(".swal-title").should(Condition.exist);

        // Optionally, you can assert the content of the SweetAlert
        String alertText = $(".swal-text").text();

        assertEquals("The carousel has been updated successfully!", alertText);

        // Print the image src of the first element - it should be different from the previous one
        String srcAfterDrag = $$(".gallery-item").first().$("img").getAttribute("src");
        System.out.println("After dragging: " + srcAfterDrag);

        assertNotEquals(srcBeforeDrag, srcAfterDrag);

        $(".swal-button").click();

        actions.release().perform();
    }

    private void drag_and_drop_to(SelenideElement source, SelenideElement destination) {
        Actions actions = new Actions(getWebDriver());

        selenium_actions(source, destination, actions, () -> {
            click_and_hold(source, actions);
            move_by(-10, 0, actions);
            move_to(destination, actions);
            release(actions);
        });
    }


    private void selenium_actions(SelenideElement source, SelenideElement destination, Actions actions, Runnable actionBlock) {
        actionBlock.run();
    }

    private void click_and_hold(SelenideElement element, Actions actions) {
        actions.clickAndHold(element).perform();
    }

    private void move_by(int xOffset, int yOffset, Actions actions) {
        actions.moveByOffset(xOffset, yOffset).perform();
    }

    private void move_to(SelenideElement element, Actions actions) {
        actions.moveToElement(element).perform();
    }

    private void release(Actions actions) {
        actions.release().perform();
    }
}
