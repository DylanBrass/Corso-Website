package com.corso.springboot.selenium_tests;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.h2.command.query.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest
public class EditOrderInformationTest {

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
    void editOrderInformationTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.cssSelector(".c24150cbf")).click();
        driver.findElement(By.id("profile-image")).click();
        driver.findElement(By.cssSelector(".past-orders-option")).click();
        {
            WebElement element = driver.findElement(By.cssSelector("li:nth-child(3) > .MuiButtonBase-root"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.cssSelector("li:nth-child(3) > .MuiButtonBase-root")).click();
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.id("profile-image")).click();
        driver.findElement(By.linkText("View Your Orders")).click();
        sleep(3000);
        // click order
//        driver.findElement(By.cssSelector(".box-body")).click();
//        driver.findElement(By.cssSelector(".col-12:nth-child(2) > .m-4")).click();
//        driver.findElement(By.cssSelector(".order-status")).click();
//        driver.findElement(By.cssSelector(".col-12:nth-child(2) > .m-4")).click();
//        sleep(3000);
        driver.findElement(By.id("edit-request")).click();
        driver.findElement(By.id("customerApartment")).click();
        driver.findElement(By.id("customerApartment")).sendKeys("478");
        driver.findElement(By.id("customerPostalCode")).click();
        driver.findElement(By.id("customerPostalCode")).sendKeys("M3C 5C4");
        sleep(7000);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.quit();
    }
}
