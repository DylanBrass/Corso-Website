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

@SpringBootTest
public class EditReviewTest {

    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;

    }


    @Test
    void updateReview() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector(".item:nth-child(4) > .m-auto")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("profile-image")).click();
        driver.findElement(By.cssSelector(".past-reviews-option")).click();
        driver.findElement(By.cssSelector(".review-message")).click();
        driver.findElement(By.cssSelector("img:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".css-dqr9h-MuiRating-label:nth-child(9)")).click();



        driver.findElement(By.id("message-edit")).click();
        driver.findElement(By.id("message-edit")).click();
        {
            WebElement element = driver.findElement(By.id("message-edit"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("message-edit")).click();
        driver.findElement(By.id("message-edit")).click();
        {
            WebElement element = driver.findElement(By.id("message-edit"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("message-edit")).click();
        driver.findElement(By.id("message-edit")).clear();

        driver.findElement(By.id("message-edit")).sendKeys("test selenide edit");
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

        assert driver.findElement(By.cssSelector(".swal-title")).isDisplayed();
        assert driver.findElement(By.cssSelector(".swal-icon--success__ring")).isDisplayed();

        driver.findElement(By.cssSelector(".swal-button")).click();

        assert driver.findElement(By.id("message-edit")).getAttribute("value").contains("test selenide edit");

        driver.findElement(By.cssSelector("img:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".css-dqr9h-MuiRating-label:nth-child(5)")).click();
        driver.findElement(By.id("message-edit")).click();
        driver.findElement(By.id("message-edit")).click();
        {
            WebElement element = driver.findElement(By.id("message-edit"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.id("message-edit")).click();
        driver.findElement(By.id("message-edit")).clear();
        driver.findElement(By.id("message-edit")).sendKeys("test");
        driver.findElement(By.cssSelector(".btn")).click();

        assert driver.findElement(By.cssSelector(".swal-title")).isDisplayed();
        assert driver.findElement(By.cssSelector(".swal-icon--success__ring")).isDisplayed();

        driver.findElement(By.cssSelector(".swal-button")).click();

        assert driver.findElement(By.id("message-edit")).getAttribute("value").contains("test");


        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.quit();
    }

}
