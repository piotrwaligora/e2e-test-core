package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.data.TestDataCleaner;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static org.apache.http.HttpStatus.SC_CREATED;

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
        if (user != null) {
            TestDataCleaner.deleteUser(user.email(), user.password());
            user = null;
        }
    }
}
