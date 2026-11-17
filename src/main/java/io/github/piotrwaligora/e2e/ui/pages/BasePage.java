package io.github.piotrwaligora.e2e.ui.pages;

import io.github.piotrwaligora.e2e.config.TestConfig;
import io.github.piotrwaligora.e2e.ui.ElementActions;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final ElementActions actions;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
    }

    protected void openPath(String path) {
        driver.get(TestConfig.baseUrl() + path);
    }
}
