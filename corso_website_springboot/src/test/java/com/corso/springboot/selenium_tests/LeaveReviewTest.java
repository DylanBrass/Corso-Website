package com.corso.springboot.selenium_tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.sleep;


@SpringBootTest
public class LeaveReviewTest {

    JavascriptExecutor js;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;

    }

    private static final Logger logger = LoggerFactory.getLogger(ViewCarouselTest.class);


    @Test
    public void LeaveReviewSelenideTest() {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.linkText("LOGIN")).click();
        driver.findElement(By.id("username")).sendKeys("dylan.brassard@outlook.com");
        driver.findElement(By.name("action")).click();
        driver.findElement(By.id("password")).sendKeys("Password!!");
        driver.findElement(By.cssSelector(".c24150cbf")).click();
        driver.findElement(By.id("profile-image")).click();
        driver.findElement(By.cssSelector(".past-orders-option")).click();
        driver.findElement(By.cssSelector(".order-status")).click();
        driver.findElement(By.cssSelector(".col-12:nth-child(1) > .m-4")).click();

        sleep(5000);

        driver.findElement(By.id("review")).click();
        driver.findElement(By.id("review")).sendKeys("Nice people and nice staff");
        assert driver.findElement(By.id("customerFullName")).getAttribute("value").equals("dylan");
        driver.findElement(By.cssSelector(".css-dqr9h-MuiRating-label:nth-child(5)")).click();
        sleep(2000);
        driver.findElement(By.cssSelector(".btn")).click();
        sleep(2000);

        assert driver.findElement(By.cssSelector(".swal-title")).isDisplayed();
        assert driver.findElement(By.cssSelector(".swal-icon--success__ring")).isDisplayed();

        driver.findElement(By.cssSelector(".swal-button")).click();
        driver.findElement(By.cssSelector(".logout-btn-text")).click();

        driver.quit();
    }

}
