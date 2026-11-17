package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.data.TestDataCleaner;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Registers a unique user before each test and removes all of its data afterwards,
 * also when the test fails. Provides API steps and verifications shared by API and UI tests.
 */
public abstract class BaseApiTest {

    protected UserRequest user;
    protected AuthResponse auth;
    protected Users users;
    protected Contacts contacts;

    @BeforeMethod
    public void registerUser() {
        user = TestDataFactory.newUser();
        Response response = new Users().register(user);
        verifyStatus(response, SC_CREATED);
        auth = response.as(AuthResponse.class);
        users = new Users(auth.token());
        contacts = new Contacts(auth.token());
    }

    @AfterMethod(alwaysRun = true)
    public void deleteTestData() {
        if (user != null) {
            TestDataCleaner.deleteUser(user.email(), user.password());
            user = null;
        }
    }

    // ---------- API steps ----------

    protected ContactResponse createContactViaApi() {
        return createContactViaApi(TestDataFactory.newContact());
    }

    @Step("Create contact via API")
    protected ContactResponse createContactViaApi(ContactRequest contact) {
        Response response = contacts.create(contact);
        verifyStatus(response, SC_CREATED);
        return response.as(ContactResponse.class);
    }

    @Step("Get contact {id} via API")
    protected ContactResponse getContactViaApi(String id) {
        Response response = contacts.getById(id);
        verifyStatus(response, SC_OK);
        return response.as(ContactResponse.class);
    }

    @Step("Get all contacts via API")
    protected List<ContactResponse> getAllContactsViaApi() {
        Response response = contacts.getAll();
        verifyStatus(response, SC_OK);
        return response.jsonPath().getList("", ContactResponse.class);
    }

    // ---------- Verifications ----------

    @Step("Verify HTTP status is {expectedStatus}")
    protected void verifyStatus(Response response, int expectedStatus) {
        assertThat(response.statusCode())
                .as("HTTP status, response body: %s", response.asString())
                .isEqualTo(expectedStatus);
    }

    @Step("Verify contact data")
    protected void verifyContactData(ContactRequest actual, ContactRequest expected) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.firstName()).as("first name").isEqualTo(expected.firstName());
            softly.assertThat(actual.lastName()).as("last name").isEqualTo(expected.lastName());
            softly.assertThat(actual.birthdate()).as("birthdate").isEqualTo(expected.birthdate());
            softly.assertThat(actual.email()).as("email").isEqualTo(expected.email());
            softly.assertThat(actual.phone()).as("phone").isEqualTo(expected.phone());
            softly.assertThat(actual.street1()).as("street 1").isEqualTo(expected.street1());
            softly.assertThat(actual.street2()).as("street 2").isEqualTo(expected.street2());
            softly.assertThat(actual.city()).as("city").isEqualTo(expected.city());
            softly.assertThat(actual.stateProvince()).as("state/province").isEqualTo(expected.stateProvince());
            softly.assertThat(actual.postalCode()).as("postal code").isEqualTo(expected.postalCode());
            softly.assertThat(actual.country()).as("country").isEqualTo(expected.country());
        });
    }

    @Step("Verify contact belongs to the current user")
    protected void verifyContactBelongsToCurrentUser(ContactResponse contact) {
        assertThat(contact.owner()).as("contact owner").isEqualTo(auth.user().id());
    }
}
