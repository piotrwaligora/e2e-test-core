package io.github.piotrwaligora.e2e.ui.driver;

import io.github.piotrwaligora.e2e.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

/**
 * Creates WebDriver instances based on configuration.
 * Driver binaries are resolved by Selenium Manager.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        Browser browser = Browser.from(TestConfig.browser());
        return switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(TestConfig.headless()));
        };
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080", "--disable-search-engine-choice-screen");
        // Prevents password manager pop-ups (e.g. breached password warning) from covering the page.
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        return options;
    }
}
