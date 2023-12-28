package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class ModifyProfileTest {

    Logger logger = Logger.getLogger(ModifyProfileTest.class.getName());
    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

    }

    @Test
    public void ModifyProfileSelenideTest() {

        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        sleep(3000);
        logger.info(driver.findElement(By.id("greeting")).getText());
        assert(driver.findElement(By.id("greeting")).getText().contains("DYLAN"));
        driver.findElement(By.id("to-profile")).click();
        driver.findElement(By.id("address")).click();
        driver.findElement(By.id("address")).sendKeys("45 Rue");
        driver.findElement(By.id("name")).sendKeys("Test");
        driver.findElement(By.id("postalCode")).sendKeys("J4R 4J4");
        driver.findElement(By.id("city")).sendKeys("La Prairie");
        driver.findElement(By.id("phone")).sendKeys("2223334556");
        driver.findElement(By.id("save-btn")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".swal-button")).click();
        driver.findElement(By.linkText("HOME")).click();
        sleep(2000);
        logger.info(driver.findElement(By.id("greeting")).getText());
        assert(driver.findElement(By.id("greeting")).getText().contains("TEST"));
        sleep(2000);
        driver.findElement(By.id("to-profile")).click();
        driver.findElement(By.id("delete-btn")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".swal-button")).click();
        sleep(2000);
        assert(Objects.equals(driver.findElement(By.id("address")).getText(), ""));
        logger.info(driver.findElement(By.id("name")).getText());
        assert(Objects.equals(driver.findElement(By.id("postalCode")).getText(), ""));
        assert(Objects.equals(driver.findElement(By.id("city")).getText(), ""));
        assert(Objects.equals(driver.findElement(By.id("phone")).getText(), ""));
        sleep(2000);
        //go back and look at title

        driver.findElement(By.id("to-profile")).click();
        driver.findElement(By.linkText("HOME")).click();
        sleep(2000);
        logger.info(driver.findElement(By.id("greeting")).getText());
        assert(!driver.findElement(By.id("greeting")).getText().contains("TEST"));

        driver.findElement(By.id("logout-btn")).click();

        sleep(2000);

        assert(driver.findElement(By.className("anonymous-banner-title")).isDisplayed());

        driver.quit();

    }

}
