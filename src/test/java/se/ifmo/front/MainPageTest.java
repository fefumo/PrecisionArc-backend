package se.ifmo.front;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPageTest {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();
        driver.get("http://localhost:3000");

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

        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void testMainPageUIElements() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement clearButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[.//span[text()='Clear Table']]")));
        assertTrue(clearButton.isDisplayed());

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[.//span[text()='Logout']]")));
        assertTrue(logoutButton.isDisplayed());

        WebElement canvas = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.tagName("canvas")));
        assertTrue(canvas.isDisplayed());
    }

    @Test
    void testClearTableButton() {
        driver.get("http://localhost:3000/main");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement clearButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Clear Table']]")));
        clearButton.click();

        WebElement emptyRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("tr.p-datatable-emptymessage")));

        String cellText = emptyRow.findElement(By.tagName("td")).getText();
        assertEquals("No available options", cellText);
    }

    @Test
    void testLogoutRedirectsToLogin() {
        driver.get("http://localhost:3000/main");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Logout']]")));
        logoutButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getCurrentUrl().equals("http://localhost:3000/"));
        assertTrue(driver.getCurrentUrl().equals("http://localhost:3000/"));
    }

    @Test
    void testAddPointAppearsInTable() {
        driver.get("http://localhost:3000/main");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement yInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("y-text")));
        yInput.clear();
        yInput.sendKeys("0");

        WebElement addPointButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Add Point']]")));
        addPointButton.click();

        // wait for anything to pop in the table
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                        "//tbody[@class='p-datatable-tbody']//tr[not(contains(@class, 'p-datatable-emptymessage'))]")));

        List<WebElement> rows = driver.findElements(By.xpath(
                "//tbody[@class='p-datatable-tbody']//tr[not(contains(@class, 'p-datatable-emptymessage'))]"));

        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> cells = lastRow.findElements(By.tagName("td"));

        String xValue = cells.get(1).getText();
        String yValue = cells.get(2).getText();
        String result = cells.get(4).getText();

        assertEquals("0", xValue);
        assertEquals("0", yValue);
        assertEquals("Hit", result);
    }

    @Test
    void testPointIsMissWithXY() {
        driver.get("http://localhost:3000/main");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement xSliderHandle = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#x-slider .p-slider-handle")));

        // focus
        xSliderHandle.click();

        Actions actions = new Actions(driver);
        for (int i = 0; i < 1; i++) {
            actions.sendKeys(Keys.ARROW_LEFT).perform();
        }

        WebElement yInput = driver.findElement(By.id("y-text"));
        yInput.clear();
        yInput.sendKeys("-1");

        WebElement addPointButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Add Point']]")));
        addPointButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                        "//tbody[@class='p-datatable-tbody']//tr[not(contains(@class, 'p-datatable-emptymessage'))]")));

        List<WebElement> rows = driver.findElements(By.xpath(
                "//tbody[@class='p-datatable-tbody']//tr[not(contains(@class, 'p-datatable-emptymessage'))]"));
        WebElement lastRow = rows.get(rows.size() - 1);
        List<WebElement> cells = lastRow.findElements(By.tagName("td"));

        assertEquals("-1", cells.get(1).getText());
        assertEquals("-1", cells.get(2).getText());
        assertEquals("Miss", cells.get(4).getText());
    }

}
