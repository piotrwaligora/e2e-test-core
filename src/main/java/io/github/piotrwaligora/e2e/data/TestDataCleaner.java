package io.github.piotrwaligora.e2e.data;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

/** Removes test users together with their contacts through the API. */
public final class TestDataCleaner {

    private TestDataCleaner() {
    }

    /**
     * Deletes the user and all of its contacts. Does nothing if the user does not exist.
     * Logs in again because a logout invalidates every token of the user.
     */
    @Step("Delete test user {email} with contacts via API")
    public static void deleteUser(String email, String password) {
        Response login = new Users().login(new LoginRequest(email, password));
        if (login.statusCode() == SC_UNAUTHORIZED) {
            return; // user was never created or has already been deleted
        }
        String token = login.then().statusCode(SC_OK).extract().as(AuthResponse.class).token();

        Contacts contacts = new Contacts(token);
        for (ContactResponse contact : contacts.getAll().then().statusCode(SC_OK)
                .extract().jsonPath().getList("", ContactResponse.class)) {
            contacts.delete(contact.id()).then().statusCode(SC_OK);
        }
        new Users(token).deleteCurrentUser().then().statusCode(SC_OK);
    }
}
