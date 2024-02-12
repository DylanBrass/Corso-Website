package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CustomerRequestsAnOrder {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:3000/");
    }

    @Test
    public void CustomerRequestsAnOrder() {
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("karinaevang@hotmail.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!", Keys.ENTER);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/dashboard"));

        driver.get("http://localhost:3000/requestOrder");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.name("serviceId")));

        driver.findElement(By.name("serviceId")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@value='2']")))
                .click();

        driver.findElement(By.id("orderDescription")).sendKeys("I need a new outlet installed in my kitchen.");
        driver.findElement(By.id("customerFullName")).sendKeys("Karina Evang");

        //auto filled attributes
        String actualAddress = driver.findElement(By.id("customerAddress")).getAttribute("value");
        String actualPostalCode = driver.findElement(By.id("customerPostalCode")).getAttribute("value");
        String actualPhoneNumber = driver.findElement(By.id("customerPhone")).getAttribute("value");
        String actualCity = driver.findElement(By.id("customerCity")).getAttribute("value");

        Assertions.assertEquals("9065 de belmont", actualAddress, "Address field was not auto-set correctly.");
        Assertions.assertEquals("H1P 2H1", actualPostalCode, "Postal code field was not auto-set correctly.");
        Assertions.assertEquals("14389959178", actualPhoneNumber, "Phone number field was not auto-set correctly.");
        Assertions.assertEquals("Montreal", actualCity, "City field was not auto-set correctly.");


        driver.findElement(By.name("action")).click();


        boolean isMessageDisplayed = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("swal-text")))
                .getText().contains("Your order has been submitted!");

        Assertions.assertTrue(isMessageDisplayed, "The success message was not displayed.");
    }
}
