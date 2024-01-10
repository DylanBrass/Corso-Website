package com.corso.springboot.selenium_tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;


@SpringBootTest
public class ViewAllOrdersWithFiltersTest {

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
    public void viewAllOrdersForUserAndStatus() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("Admin");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
        sleep(2000);

        driver.get("http://localhost:3000/orders/all");

        driver.findElement(By.cssSelector(".col-12 > .dropdown-container > .dropdown-header")).click();
        driver.findElement(By.cssSelector(".dropdown-option:nth-child(5)")).click();
        driver.findElement(By.cssSelector(".w-100 > .dropdown-container > .dropdown-header")).click();
        driver.findElement(By.cssSelector(".dropdown-option:nth-child(2)")).click();
        driver.findElement(By.id("search-value")).click();
        driver.findElement(By.id("search-value")).sendKeys("dyl");
        driver.findElement(By.cssSelector("button > img")).click();
        sleep(3000);

        new Actions(driver).moveToElement(driver.findElements(By.className("user-row")).get(0)).click().perform();

        driver.findElement(By.cssSelector(".m-4 > .user-selection")).click();
        {
            WebElement dropdown = driver.findElement(By.id("pageSize"));
            dropdown.findElement(By.xpath("//option[. = '5']")).click();
        }

        assert driver.findElements(By.className("orders-tile")).size() == 2;

        driver.findElement(By.id("logout-btn")).click();

        driver.quit();
    }

}
