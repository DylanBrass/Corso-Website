package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class AddEntryToGalleryTest {

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
    public void uploadGalleryTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.cssSelector(".row:nth-child(3) > .col-12:nth-child(1) .d-block")).click();

        // count gallery items
        int galleryItems = driver.findElements(By.cssSelector(".gallery-item")).size();

        logger.info("Gallery items: " + galleryItems);

        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).sendKeys("Testing for selenide");
        driver.findElement(By.id("image")).sendKeys("/home/dylanbrass/Downloads/test-image-carousel-3.png");

        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        sleep(2000);


        driver.findElement(By.cssSelector(".btn")).click();

        sleep(5000);

        driver.findElement(By.cssSelector(".swal-button")).click();

        // count gallery items again

        int galleryItemsAfter = driver.findElements(By.cssSelector(".gallery-item")).size();

        logger.info("Gallery items after: " + galleryItemsAfter);

        assert galleryItemsAfter == galleryItems + 1;

        js.executeScript("window.scrollTo(0, 0)");

        sleep(2000);
        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.close();
    }
}
