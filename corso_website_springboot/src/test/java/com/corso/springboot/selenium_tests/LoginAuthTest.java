package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class LoginAuthTest {
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

    }

    private static final Logger logger = LoggerFactory.getLogger(ViewCarouselTest.class);

    @Test
    public void LoginAuthGoToDashBoardTest(){
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1740, 1091));
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("Admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        driver.findElement(By.linkText("DASHBOARD")).click();
        driver.get("http://localhost:3000/create/admin");

        assert driver.findElement(By.id("add-admin-form")).isDisplayed();

        driver.findElement(By.id("logout-btn")).click();

        sleep(2000);

        assert driver.findElement(By.className("banner-box")).isDisplayed();

        driver.quit();
    }




}
