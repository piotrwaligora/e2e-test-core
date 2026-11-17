package io.github.piotrwaligora.e2e.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactListPage extends BasePage {

    private static final String PATH = "/contactList";

    private static final By ADD_CONTACT = By.id("add-contact");

    public ContactListPage(WebDriver driver) {
        super(driver);
    }

    public ContactListPage open() {
        openPath(PATH);
        return waitUntilLoaded();
    }

    public ContactListPage waitUntilLoaded() {
        actions.waitForUrlContains(PATH);
        actions.waitFor(ADD_CONTACT);
        return this;
    }

    public AddContactPage clickAddContact() {
        actions.clickOn(ADD_CONTACT);
        return new AddContactPage(driver).waitUntilLoaded();
    }

    /** Waits for the contact row and returns its visible text. */
    public String getContactRowText(String fullName) {
        return actions.getText(contactRow(fullName));
    }

    public ContactDetailsPage openContact(String fullName) {
        actions.clickOn(contactRow(fullName));
        return new ContactDetailsPage(driver).waitUntilLoaded();
    }

    private static By contactRow(String fullName) {
        return By.xpath("//tr[@class='contactTableBodyRow'][td[normalize-space()='" + fullName + "']]");
    }
}
