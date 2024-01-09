package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.Assert.assertEquals;


@SpringBootTest
public class ViewProfileOrdersTest {
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;

    }

    private static final Logger logger = LoggerFactory.getLogger(ViewCarouselTest.class);

    @Test
    public void ViewProfileTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();

        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.cssSelector(".c5faccce1")).click();
        driver.findElement(By.id("profile-image")).click();
        {
            WebElement element = driver.findElement(By.id("profile-image"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        //scroll to bottom cause selenide is dumb

        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        sleep(1000);

        {
            WebElement dropdown = driver.findElement(By.id("pageSize"));
            dropdown.findElement(By.xpath("//option[. = '5']")).click();
        }

        //make sure 5 .box-tile are present
        int xpathCount = driver.findElements(By.className("orders-tile")).size();

        Assertions.assertEquals(5,xpathCount);

        //scroll to top
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0)");

        sleep(1000);

        driver.findElement(By.id("logout-btn")).click();

        driver.quit();
    }
}
