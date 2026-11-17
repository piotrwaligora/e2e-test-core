package io.github.piotrwaligora.e2e.ui.pages;

import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactDetailsPage extends BasePage {

    private static final String PATH = "/contactDetails";

    private static final By FIRST_NAME = By.id("firstName");
    private static final By LAST_NAME = By.id("lastName");
    private static final By BIRTHDATE = By.id("birthdate");
    private static final By EMAIL = By.id("email");
    private static final By PHONE = By.id("phone");
    private static final By STREET_1 = By.id("street1");
    private static final By STREET_2 = By.id("street2");
    private static final By CITY = By.id("city");
    private static final By STATE_PROVINCE = By.id("stateProvince");
    private static final By POSTAL_CODE = By.id("postalCode");
    private static final By COUNTRY = By.id("country");

    public ContactDetailsPage(WebDriver driver) {
        super(driver);
    }

    /** Contact data is fetched asynchronously after the page loads. */
    public ContactDetailsPage waitUntilLoaded() {
        actions.waitForUrlContains(PATH);
        actions.waitForNonEmptyText(FIRST_NAME);
        return this;
    }

    @Step("Read contact details")
    public ContactRequest getContact() {
        return new ContactRequest(
                actions.getText(FIRST_NAME),
                actions.getText(LAST_NAME),
                actions.getText(BIRTHDATE),
                actions.getText(EMAIL),
                actions.getText(PHONE),
                actions.getText(STREET_1),
                actions.getText(STREET_2),
                actions.getText(CITY),
                actions.getText(STATE_PROVINCE),
                actions.getText(POSTAL_CODE),
                actions.getText(COUNTRY));
    }
}
