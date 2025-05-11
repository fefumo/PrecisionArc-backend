package se.ifmo.front;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.ifmo.util.RandomStringGenerator;

public class RegisterTest {
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
    void testSuccessfulRegistration(){
        String randomName = RandomStringGenerator.generateString();
        String randomPassword = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys(randomName);

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys(randomPassword);

        // Enter passwordx2
        WebElement confirmPasswordInput = driver.findElement(By.cssSelector("#confirmPassword input"));
        confirmPasswordInput.sendKeys(randomPassword);
        
        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Wait until URL changes to /
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));

        // Assert final URL
        assertTrue(driver.getCurrentUrl().contentEquals("http://localhost:3000/"));
    }

    @Test
    void testEmptyUsernameRegistration(){
        String randomPassword = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys("");

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys(randomPassword);

        // Enter passwordx2
        WebElement confirmPasswordInput = driver.findElement(By.cssSelector("#confirmPassword input"));
        confirmPasswordInput.sendKeys(randomPassword);
        
        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Wait until URL DOES NOT change
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/register"));

        // Assert final URL
        assertTrue(driver.getCurrentUrl().contentEquals("http://localhost:3000/register"));
    }

    @Test
    void testEmptyPasswordRegistration(){
        String randomName = RandomStringGenerator.generateString();
        String randomPassword = RandomStringGenerator.generateString();

        driver.get("http://localhost:3000/register");

        // Enter nickname
        WebElement nicknameInput = driver.findElement(By.id("username"));
        nicknameInput.sendKeys(randomName);

        // Enter password
        WebElement passwordInput = driver.findElement(By.cssSelector("#password input"));
        passwordInput.sendKeys("");

        // Enter passwordx2
        WebElement confirmPasswordInput = driver.findElement(By.cssSelector("#confirmPassword input"));
        confirmPasswordInput.sendKeys(randomPassword);
        
        // Submit the form
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        loginButton.click();

        // Wait until URL DOES NOT change
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/register"));

        // Assert final URL
        assertTrue(driver.getCurrentUrl().contentEquals("http://localhost:3000/register"));
    }

    
}
