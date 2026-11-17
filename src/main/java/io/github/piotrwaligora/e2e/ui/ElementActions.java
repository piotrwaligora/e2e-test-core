package io.github.piotrwaligora.e2e.ui;

import io.github.piotrwaligora.e2e.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Element interactions synchronized with {@link FluentWait}.
 * Every action locates the element again and waits until it is ready, so page objects
 * never call {@code driver.findElement} directly. Implicit waits are intentionally not used.
 */
public class ElementActions {

    private final WebDriver driver;
    private final FluentWait<WebDriver> wait;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        // WebDriverWait is a FluentWait that already ignores NotFoundException while polling.
        this.wait = new WebDriverWait(driver, TestConfig.uiTimeout(), TestConfig.uiPolling())
                .ignoring(StaleElementReferenceException.class);
    }

    /** Waits until the element is present and visible. */
    public WebElement waitFor(By locator) {
        return waitFor(locator, ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits until the element is visible and enabled. */
    public WebElement waitForClickable(By locator) {
        return waitFor(locator, ExpectedConditions.elementToBeClickable(locator));
    }

    public void clickOn(By locator) {
        waitForClickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = waitForClickable(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return waitFor(locator).getText();
    }

    /** Waits until the element has any text, e.g. when data is loaded asynchronously. */
    public void waitForNonEmptyText(By locator) {
        waitFor(locator, ExpectedConditions.not(ExpectedConditions.textToBe(locator, "")));
    }

    public void waitForUrlContains(String fragment) {
        wait.withMessage("URL to contain: " + fragment)
                .until(ExpectedConditions.urlContains(fragment));
    }

    /** Checks visibility immediately, without waiting. */
    public boolean isDisplayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    private <T> T waitFor(By locator, ExpectedCondition<T> condition) {
        return wait.withMessage("element: " + locator).until(condition);
    }
}
