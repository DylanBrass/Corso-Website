package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class CancelOrderRequestCustomerTest {
    Logger logger = Logger.getLogger(ModifyProfileTest.class.getName());
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;

    }

    @Test
    public void cancelOrderRequestCustomerTest()  {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.cssSelector(".c24150cbf")).click();
        driver.findElement(By.id("profile-image")).click();
        {
            WebElement element = driver.findElement(By.id("profile-image"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector(".past-orders-option")).click();
        driver.findElement(By.cssSelector(".box-body")).click();
        sleep(3000);
        driver.findElement(By.cssSelector(".col-12:nth-child(3) > .m-4")).click();
        driver.findElement(By.id("reason")).sendKeys("This is a selenide cancel");
        driver.findElement(By.cssSelector(".btn")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".btn"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }



        sleep(7000);
        assert driver.findElement(By.cssSelector(".swal-title")).isDisplayed();
        assert driver.findElement(By.cssSelector(".swal-icon--success__ring")).isDisplayed();

        driver.findElement(By.cssSelector(".swal-button")).click();

        sleep(2000);
        assert driver.findElement(By.className("order-status")).isDisplayed();
        assert driver.findElement(By.className("order-status")).getText().contains("Cancelled");

        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.quit();
    }
}
