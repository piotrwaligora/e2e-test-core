package io.github.piotrwaligora.e2e.bdd.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.bdd.context.TestContext;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class ContactSteps {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final TestContext context;

    public ContactSteps(TestContext context) {
        this.context = context;
    }

    @Given("a registered user")
    public void aRegisteredUser() {
        UserRequest user = TestDataFactory.newUser();
        context.setUser(user); // set before the call so that the After hook can clean up
        Response response = new Users().register(user);
        verifyStatus(response, SC_CREATED);
        context.setContacts(new Contacts(response.as(AuthResponse.class).token()));
    }

    @Given("the user has a contact")
    public void theUserHasAContact() {
        context.setContact(createContact(TestDataFactory.newContact()));
    }

    @When("the user adds a contact with details:")
    public void theUserAddsAContact(Map<String, String> details) {
        ContactRequest contact = MAPPER.convertValue(details, ContactRequest.class);
        context.setExpectedContact(contact);
        context.setContact(createContact(contact));
    }

    @When("the user updates the contact with:")
    public void theUserUpdatesTheContact(Map<String, String> changes) {
        ContactResponse current = context.getContact();
        ContactRequest updated = merge(current.toRequest(), changes);
        context.setExpectedContact(updated);

        verifyStatus(context.getContacts().update(current.id(), updated), SC_OK);
    }

    @Then("the contact is saved with the submitted details")
    public void theContactIsSaved() {
        verifyStoredContactMatchesExpected();
    }

    @Then("the contact has the updated details")
    public void theContactHasTheUpdatedDetails() {
        verifyStoredContactMatchesExpected();
    }

    @Then("the contact list contains {int} contact(s)")
    public void theContactListContains(int expectedCount) {
        Response response = context.getContacts().getAll();
        verifyStatus(response, SC_OK);
        List<ContactResponse> contacts = response.jsonPath().getList("", ContactResponse.class);
        assertThat(contacts).as("contact list").hasSize(expectedCount);
    }

    // ---------- Helpers ----------

    private ContactResponse createContact(ContactRequest contact) {
        Response response = context.getContacts().create(contact);
        verifyStatus(response, SC_CREATED);
        return response.as(ContactResponse.class);
    }

    /** Returns a copy of the contact with the given fields replaced. */
    private static ContactRequest merge(ContactRequest contact, Map<String, String> changes) {
        Map<String, Object> fields = new HashMap<>(MAPPER.convertValue(contact, new TypeReference<Map<String, Object>>() {
        }));
        fields.putAll(changes);
        return MAPPER.convertValue(fields, ContactRequest.class);
    }

    private void verifyStoredContactMatchesExpected() {
        Response response = context.getContacts().getById(context.getContact().id());
        verifyStatus(response, SC_OK);
        ContactRequest stored = response.as(ContactResponse.class).toRequest();

        // Recursive comparison reports every mismatching field at once.
        assertThat(stored).as("stored contact")
                .usingRecursiveComparison()
                .isEqualTo(context.getExpectedContact());
    }

    private static void verifyStatus(Response response, int expectedStatus) {
        assertThat(response.statusCode())
                .as("HTTP status, response body: %s", response.asString())
                .isEqualTo(expectedStatus);
    }
}
