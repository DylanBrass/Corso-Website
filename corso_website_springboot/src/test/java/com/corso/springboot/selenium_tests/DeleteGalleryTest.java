package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class DeleteGalleryTest {
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;

    }


    @Test
    void deleteGalleryTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.cssSelector(".row:nth-child(3) > .col-12:nth-child(1) .col-5")).click();
        driver.findElement(By.cssSelector(".gallery-item:nth-child(2) > .mt-2")).click();
        driver.findElement(By.cssSelector(".delete-galleryImage-btn > img")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".swal-button--confirm")).click();
        sleep(2000);

        driver.findElement(By.cssSelector(".swal-button")).click();
        sleep(2000);
        driver.quit();

    }

}
