package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.github.piotrwaligora.e2e.api.model.UserResponse;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class UserApiTest extends BaseApiTest {

    @Test(description = "Registered user can read own profile")
    public void shouldReturnProfileOfRegisteredUser() {
        UserResponse profile = users.getProfile()
                .then().statusCode(SC_OK)
                .extract().as(UserResponse.class);

        assertEquals(profile.id(), auth.user().id());
        assertEquals(profile.firstName(), user.firstName());
        assertEquals(profile.lastName(), user.lastName());
        assertEquals(profile.email(), user.email());
    }

    @Test(description = "User can log in with valid credentials")
    public void shouldLogInWithValidCredentials() {
        AuthResponse login = new Users().login(new LoginRequest(user.email(), user.password()))
                .then().statusCode(SC_OK)
                .extract().as(AuthResponse.class);

        assertFalse(login.token().isBlank(), "Token should not be blank");
        assertEquals(login.user().email(), user.email());
    }

    @Test(description = "Login with a wrong password is rejected")
    public void shouldRejectLoginWithInvalidPassword() {
        new Users().login(new LoginRequest(user.email(), "wrong-" + user.password()))
                .then().statusCode(SC_UNAUTHORIZED);
    }

    @Test(description = "Registration with an already used email is rejected")
    public void shouldRejectRegistrationWithDuplicateEmail() {
        new Users().register(user)
                .then().statusCode(SC_BAD_REQUEST);
    }

    @Test(description = "Logout invalidates the token")
    public void shouldInvalidateTokenOnLogout() {
        users.logout().then().statusCode(SC_OK);

        users.getProfile().then().statusCode(SC_UNAUTHORIZED);
    }

    @Test(description = "Deleted user can no longer log in")
    public void shouldDeleteCurrentUser() {
        users.deleteCurrentUser().then().statusCode(SC_OK);

        new Users().login(new LoginRequest(user.email(), user.password()))
                .then().statusCode(SC_UNAUTHORIZED);
    }
}
