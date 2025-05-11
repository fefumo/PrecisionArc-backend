package se.ifmo.front;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void testSuccessfulLogin() {
        driver.get("http://localhost:3000/");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys("asd");

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys("asd");

        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Wait until URL changes to /main
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getCurrentUrl().equals("http://localhost:3000/main"));

        // Assert final URL
        assertTrue(driver.getCurrentUrl().equals("http://localhost:3000/main"));
    }

    @Test
    void testUnSuccessfulLogin() {
        driver.get("http://localhost:3000/");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys("1111");

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys("1111");

        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error-message")));

        String errorText = alert.getText();
        assertTrue(errorText.contains("Login"));
        assertTrue(driver.getCurrentUrl().equals("http://localhost:3000/"));
    }

    @Test
    public void testEmptyUsername() {
        driver.get("http://localhost:3000/");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys("");

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys("meow");

        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(
                currentUrl.equals("http://localhost:3000/"),
                "Should stay on login page if required fields are empty");
    }

    @Test
    public void testEmptyPassword() {
        driver.get("http://localhost:3000/");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys("meow");

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys("");

        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        String currentUrl = driver.getCurrentUrl();
        assertTrue(
                currentUrl.equals("http://localhost:3000/"),
                "Should stay on login page if required fields are empty");
    }

}
