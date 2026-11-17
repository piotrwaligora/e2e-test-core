package io.github.piotrwaligora.e2e.ui;

import io.github.piotrwaligora.e2e.api.BaseApiTest;
import io.github.piotrwaligora.e2e.ui.driver.DriverFactory;
import io.github.piotrwaligora.e2e.ui.pages.ContactListPage;
import io.github.piotrwaligora.e2e.ui.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Adds a browser on top of the API setup: the user is registered and removed through the API,
 * the browser is started before and closed after each test.
 */
public abstract class BaseUiTest extends BaseApiTest {

    protected WebDriver driver;

    @BeforeMethod
    public void startBrowser() {
        driver = DriverFactory.createDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    // ---------- UI steps ----------

    protected ContactListPage loginViaUi() {
        return new LoginPage(driver).open()
                .loginAs(user.email(), user.password());
    }

    /** Skips the login form by reusing the token of the user registered through the API. */
    protected ContactListPage loginWithApiToken() {
        return new LoginPage(driver).open()
                .loginWithToken(auth.token());
    }
}
