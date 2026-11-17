package io.github.piotrwaligora.e2e.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String PATH = "/";
    private static final String TOKEN_COOKIE = "token";

    private static final By EMAIL = By.id("email");
    private static final By PASSWORD = By.id("password");
    private static final By SUBMIT = By.id("submit");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open login page")
    public LoginPage open() {
        openPath(PATH);
        actions.waitFor(SUBMIT);
        return this;
    }

    @Step("Log in through the login form as {email}")
    public ContactListPage loginAs(String email, String password) {
        actions.type(EMAIL, email);
        actions.type(PASSWORD, password);
        actions.clickOn(SUBMIT);
        return new ContactListPage(driver).waitUntilLoaded();
    }

    /** Skips the login form by setting the API token as the session cookie used by the app. */
    @Step("Log in with API token cookie")
    public ContactListPage loginWithToken(String token) {
        driver.manage().addCookie(new Cookie(TOKEN_COOKIE, token, "/"));
        return new ContactListPage(driver).open();
    }
}
