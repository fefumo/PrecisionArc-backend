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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/main"));

        // Assert final URL
        assertTrue(driver.getCurrentUrl().contains("/main"));
    
    }
    
}
