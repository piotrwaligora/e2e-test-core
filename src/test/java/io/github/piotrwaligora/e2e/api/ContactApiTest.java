package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactApiTest extends BaseApiTest {

    @Test(description = "Contact is created with all submitted fields")
    public void shouldCreateContact() {
        ContactRequest contact = TestDataFactory.newContact();

        ContactResponse created = createContactViaApi(contact);

        verifyContactBelongsToCurrentUser(created);
        verifyContactData(created.toRequest(), contact);
    }

    @Test(description = "Contact list returns every contact of the user")
    public void shouldListAllContacts() {
        ContactResponse first = createContactViaApi();
        ContactResponse second = createContactViaApi();

        List<ContactResponse> allContacts = getAllContactsViaApi();

        verifyContactsListed(allContacts, first, second);
    }

    @Test(description = "Contact can be read by id")
    public void shouldGetContactById() {
        ContactResponse created = createContactViaApi();

        ContactResponse fetched = getContactViaApi(created.id());

        verifySameContact(fetched, created);
    }

    @Test(description = "PUT replaces all contact fields")
    public void shouldUpdateContactWithPut() {
        ContactResponse created = createContactViaApi();
        ContactRequest replacement = TestDataFactory.newContact();

        updateContactViaApi(created.id(), replacement);

        verifyContactData(getContactViaApi(created.id()).toRequest(), replacement);
    }

    @Test(description = "PATCH changes only the submitted fields")
    public void shouldPartiallyUpdateContactWithPatch() {
        ContactResponse created = createContactViaApi();

        changeFirstNameViaApi(created.id(), "Anna");

        verifyContactData(getContactViaApi(created.id()).toRequest(), created.toRequest().withFirstName("Anna"));
    }

    @Test(description = "Deleted contact is no longer available")
    public void shouldDeleteContact() {
        ContactResponse created = createContactViaApi();

        deleteContactViaApi(created.id());

        verifyContactDoesNotExist(created.id());
    }

    @Test(description = "Contact without the required last name is rejected")
    public void shouldRejectContactWithoutLastName() {
        ContactRequest withoutLastName = TestDataFactory.newContact().withLastName(null);

        Response response = contacts.create(withoutLastName);

        verifyRequiredFieldError(response, "lastName");
    }

    @Test(description = "Contacts cannot be read without a token")
    public void shouldRejectRequestWithoutToken() {
        Response response = new Contacts().getAll();

        verifyStatus(response, SC_UNAUTHORIZED);
    }

    // ---------- Steps ----------

    @Step("Replace contact {id} via API")
    private void updateContactViaApi(String id, ContactRequest contact) {
        verifyStatus(contacts.update(id, contact), SC_OK);
    }

    @Step("Change first name of contact {id} to {firstName} via API")
    private void changeFirstNameViaApi(String id, String firstName) {
        verifyStatus(contacts.patch(id, Map.of("firstName", firstName)), SC_OK);
    }

    @Step("Delete contact {id} via API")
    private void deleteContactViaApi(String id) {
        verifyStatus(contacts.delete(id), SC_OK);
    }

    // ---------- Verifications ----------

    @Step("Verify contact list")
    private void verifyContactsListed(List<ContactResponse> actual, ContactResponse... expected) {
        assertThat(actual).as("contact list").containsExactlyInAnyOrder(expected);
    }

    @Step("Verify fetched contact")
    private void verifySameContact(ContactResponse actual, ContactResponse expected) {
        assertThat(actual).as("fetched contact").isEqualTo(expected);
    }

    @Step("Verify contact {id} does not exist")
    private void verifyContactDoesNotExist(String id) {
        verifyStatus(contacts.getById(id), SC_NOT_FOUND);
    }

    @Step("Verify required field error for {field}")
    private void verifyRequiredFieldError(Response response, String field) {
        verifyStatus(response, SC_BAD_REQUEST);
        assertThat(response.jsonPath().getString("errors." + field + ".kind"))
                .as("validation error for %s", field)
                .isEqualTo("required");
    }
}
