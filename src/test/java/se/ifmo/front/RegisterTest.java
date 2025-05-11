package se.ifmo.front;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.ifmo.util.RandomStringGenerator;

public class RegisterTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void clickSubmitSafely() {
        try {
            // Wait for hint to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("p-password-info")));
        } catch (Exception e) {
            System.out.println("Warning: .p-password-info did not disappear in time.");
        }

        WebElement button = driver.findElement(By.cssSelector("button[type='submit']"));
        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element was still obscured. Using JS click.");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    @Test
    void testSuccessfulRegistration() {
        String randomName = RandomStringGenerator.generateString();
        String randomPassword = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        driver.findElement(By.id("username")).sendKeys(randomName);
        driver.findElement(By.cssSelector("#password input")).sendKeys(randomPassword);
        driver.findElement(By.cssSelector("#confirmPassword input")).sendKeys(randomPassword);

        clickSubmitSafely();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        assertEquals("http://localhost:3000/", driver.getCurrentUrl());
    }

    @Test
    void testEmptyUsernameRegistration() {
        String randomPassword = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        driver.findElement(By.id("username")).sendKeys("");
        driver.findElement(By.cssSelector("#password input")).sendKeys(randomPassword);
        driver.findElement(By.cssSelector("#confirmPassword input")).sendKeys(randomPassword);

        clickSubmitSafely();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/register"));
        assertEquals("http://localhost:3000/register", driver.getCurrentUrl());
    }

    @Test
    void testEmptyPasswordRegistration() {
        String randomName = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        driver.findElement(By.id("username")).sendKeys(randomName);
        driver.findElement(By.cssSelector("#password input")).sendKeys("");
        driver.findElement(By.cssSelector("#confirmPassword input")).sendKeys("somepass");

        clickSubmitSafely();

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/register"));
        assertEquals("http://localhost:3000/register", driver.getCurrentUrl());
    }

    @Test
    void testRegisterWithExistingName() {
        driver.get("http://localhost:3000/register");

        driver.findElement(By.id("username")).sendKeys("asd");
        driver.findElement(By.cssSelector("#password input")).sendKeys("asd");
        driver.findElement(By.cssSelector("#confirmPassword input")).sendKeys("asd");

        clickSubmitSafely();

        WebElement errorMessage = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[text()='Registration failed.']")
            )
        );

        assertEquals("http://localhost:3000/register", driver.getCurrentUrl());
        assertEquals("Registration failed.", errorMessage.getText());
    }
}
