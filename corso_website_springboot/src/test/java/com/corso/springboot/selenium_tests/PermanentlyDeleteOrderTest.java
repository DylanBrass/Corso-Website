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

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class PermanentlyDeleteOrderTest {
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
    public void PermanentlyDeleteOrderSelenideTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector(".item:nth-child(4) > .m-auto")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.cssSelector(".row:nth-child(2) > .col-12:nth-child(1) .col-7")).click();
        driver.findElement(By.cssSelector(".col-12 > .dropdown-container > .dropdown-header")).click();
        driver.findElement(By.cssSelector(".dropdown-option:nth-child(6)")).click();
        driver.findElement(By.cssSelector(".m-4 > .user-selection")).click();
        driver.findElement(By.cssSelector(".orders-tile:nth-child(5) > .box-body")).click();
        driver.findElement(By.cssSelector(".m-4")).click();
        sleep(2000);
        logger.info(driver.findElement(By.cssSelector(".swal-text")).getText());
        assert (driver.findElement(By.cssSelector(".swal-text")).getText().contains("Are you sure you want to delete this order,"));
        driver.findElement(By.cssSelector(".swal-button--confirm")).click();
        sleep(2000);

        assert driver.findElement(By.cssSelector(".swal-title")).isDisplayed();
        assert driver.findElement(By.cssSelector(".swal-icon--success__ring")).isDisplayed();
        driver.findElement(By.cssSelector(".swal-button")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.quit();

    }
    }
