package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.codeborne.selenide.Selenide.sleep;


@SpringBootTest
public class UpdateAnOrderTest {

    Logger logger = Logger.getLogger(ModifyProfileTest.class.getName());
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
    }



    @Test
    public void updateOrdersTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector(".item:nth-child(4) > .m-auto")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.cssSelector(".row:nth-child(1) > .col-12:nth-child(2) .d-block")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".orders-tile:nth-child(2) > .box-body")).click();
        //scroll down
        js.executeScript("window.scrollBy(0,1000)");
        sleep(2000);

        driver.findElement(By.cssSelector(".edit-order-btn")).click();
        sleep(2000);
        String value = String.valueOf((Integer.parseInt(driver.findElement(By.id("estimationWorked")).getAttribute("value")) + 1));

        driver.findElement(By.id("estimationWorked")).clear();
        driver.findElement(By.id("estimationWorked")).sendKeys(value);
        driver.findElement(By.id("estimationWorked")).click();

        String value2 = String.valueOf((Integer.parseInt(driver.findElement(By.cssSelector(".display-hours-worked")).getAttribute("value")) + 1));
        driver.findElement(By.cssSelector(".display-hours-worked")).clear();
        driver.findElement(By.cssSelector(".display-hours-worked")).sendKeys(value2);
        driver.findElement(By.cssSelector(".display-hours-worked")).click();
        driver.findElement(By.id("serviceId")).click();
        {
            WebElement dropdown = driver.findElement(By.id("serviceId"));
            dropdown.findElement(By.xpath("//option[. = 'Emergency Electrical Repairs']")).click();
        }
        driver.findElement(By.name("updateOrderButton")).click();
        sleep(4000);

        WebElement successMessage = driver.findElement(By.cssSelector(".swal-title"));
        assertEquals("Your order has been updated successfully!", successMessage.getText(), "Order update confirmation message");

        driver.findElement(By.cssSelector(".swal-button")).click();
        sleep(2000);
        js.executeScript("window.scrollBy(0,-1000)");
        sleep(2000);
        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.quit();
    }

}
