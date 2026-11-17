package io.github.piotrwaligora.e2e.ui.pages;

import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddContactPage extends BasePage {

    private static final String PATH = "/addContact";

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
    private static final By SUBMIT = By.id("submit");

    public AddContactPage(WebDriver driver) {
        super(driver);
    }

    public AddContactPage waitUntilLoaded() {
        actions.waitForUrlContains(PATH);
        actions.waitFor(FIRST_NAME);
        return this;
    }

    @Step("Fill in and submit the Add Contact form")
    public ContactListPage addContact(ContactRequest contact) {
        actions.type(FIRST_NAME, contact.firstName());
        actions.type(LAST_NAME, contact.lastName());
        actions.type(BIRTHDATE, contact.birthdate());
        actions.type(EMAIL, contact.email());
        actions.type(PHONE, contact.phone());
        actions.type(STREET_1, contact.street1());
        actions.type(STREET_2, contact.street2());
        actions.type(CITY, contact.city());
        actions.type(STATE_PROVINCE, contact.stateProvince());
        actions.type(POSTAL_CODE, contact.postalCode());
        actions.type(COUNTRY, contact.country());
        actions.clickOn(SUBMIT);
        return new ContactListPage(driver).waitUntilLoaded();
    }
}
