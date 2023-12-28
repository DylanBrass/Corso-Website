package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.logging.Logger;

import static com.codeborne.selenide.Selenide.sleep;

@SpringBootTest
public class CreateExternalOrderTest {

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
    public void createExternalOrderTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(org.openqa.selenium.By.linkText("LOGIN")).click();
        driver.findElement(org.openqa.selenium.By.id("username")).sendKeys("Admin");
        driver.findElement(org.openqa.selenium.By.name("action")).click();
        driver.findElement(org.openqa.selenium.By.id("password")).sendKeys("Password!!");
        driver.findElement(org.openqa.selenium.By.id("password")).sendKeys(org.openqa.selenium.Keys.ENTER);

        driver.findElement(By.cssSelector(".row:nth-child(4) > .col-4:nth-child(1) .col-12:nth-child(1)")).click();
        driver.findElement(By.id("search-value")).click();
        driver.findElement(By.id("search-value")).sendKeys("dy");
        driver.findElement(By.cssSelector("button > img")).click();
        sleep(3000);

        driver.findElement(By.ByXPath.xpath("//*[@id='root']/div/div[2]/div/div/table/tbody/tr[4]")).
                click();
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");

        sleep(1000);
        logger.info("City : " + driver.findElement(By.id("customerCity")).getAttribute("value"));
        assert Objects.equals(driver.findElement(By.id("customerCity")).getAttribute("value"), "La Prairie");

        driver.findElement(By.id("submit-order")).click();

        sleep(2000);
        driver.findElement(By.cssSelector(".swal-button")).click();
        sleep(2000);

        assert driver.getCurrentUrl().equals("http://localhost:3000/dashboard");
        driver.quit();
    }
}
