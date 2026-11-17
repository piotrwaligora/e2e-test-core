package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

/**
 * Registers a unique user before each test and removes all of its data afterwards,
 * also when the test fails.
 */
public abstract class BaseApiTest {

    protected UserRequest user;
    protected AuthResponse auth;
    protected Users users;
    protected Contacts contacts;

    @BeforeMethod
    public void registerUser() {
        user = TestDataFactory.newUser();
        auth = new Users().register(user)
                .then().statusCode(SC_CREATED)
                .extract().as(AuthResponse.class);
        users = new Users(auth.token());
        contacts = new Contacts(auth.token());
    }

    @AfterMethod(alwaysRun = true)
    public void deleteTestData() {
        if (user == null) {
            return;
        }
        // Log in again: the test may have logged out, which invalidates every token of the user.
        Response login = new Users().login(new LoginRequest(user.email(), user.password()));
        user = null;
        if (login.statusCode() == SC_UNAUTHORIZED) {
            return; // registration failed or the test already deleted the user
        }
        String token = login.then().statusCode(SC_OK).extract().as(AuthResponse.class).token();

        Contacts cleanupContacts = new Contacts(token);
        for (ContactResponse contact : cleanupContacts.getAll().then().statusCode(SC_OK)
                .extract().jsonPath().getList("", ContactResponse.class)) {
            cleanupContacts.delete(contact.id()).then().statusCode(SC_OK);
        }
        new Users(token).deleteCurrentUser().then().statusCode(SC_OK);
    }
}
