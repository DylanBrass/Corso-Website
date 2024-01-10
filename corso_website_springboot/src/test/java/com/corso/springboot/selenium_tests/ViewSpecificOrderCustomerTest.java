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

@SpringBootTest
public class ViewSpecificOrderCustomerTest {

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
    public void viewSpecificOrder() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.cssSelector(".c5faccce1")).click();
        driver.findElement(By.id("profile-image")).click();
        driver.findElement(By.cssSelector(".past-orders-option")).click();
        driver.findElement(By.cssSelector(".box-body")).click();

        assert driver.findElement(By.id("title")).isDisplayed();

        assert driver.findElement(By.id("title")).getText().contains("1E92BF");

        driver.findElement(By.id("logout-btn")).click();

        driver.quit();
    }

}
