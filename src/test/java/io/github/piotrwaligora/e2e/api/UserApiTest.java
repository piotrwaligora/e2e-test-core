package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.github.piotrwaligora.e2e.api.model.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

public class UserApiTest extends BaseApiTest {

    @Test(description = "Registered user can read own profile")
    public void shouldReturnProfileOfRegisteredUser() {
        UserResponse profile = getProfileViaApi();

        verifyProfileOfRegisteredUser(profile);
    }

    @Test(description = "User can log in with valid credentials")
    public void shouldLogInWithValidCredentials() {
        Response response = loginViaApi(user.password());

        verifyLoggedInAsRegisteredUser(response);
    }

    @Test(description = "Login with a wrong password is rejected")
    public void shouldRejectLoginWithInvalidPassword() {
        Response response = loginViaApi("wrong-" + user.password());

        verifyStatus(response, SC_UNAUTHORIZED);
    }

    @Test(description = "Registration with an already used email is rejected")
    public void shouldRejectRegistrationWithDuplicateEmail() {
        Response response = new Users().register(user);

        verifyStatus(response, SC_BAD_REQUEST);
    }

    @Test(description = "Logout invalidates the token")
    public void shouldInvalidateTokenOnLogout() {
        logoutViaApi();

        verifyTokenIsRejected();
    }

    @Test(description = "Deleted user can no longer log in")
    public void shouldDeleteCurrentUser() {
        deleteCurrentUserViaApi();

        verifyUserCannotLogIn();
    }

    // ---------- Steps ----------

    @Step("Get user profile via API")
    private UserResponse getProfileViaApi() {
        Response response = users.getProfile();
        verifyStatus(response, SC_OK);
        return response.as(UserResponse.class);
    }

    @Step("Log in via API")
    private Response loginViaApi(String password) {
        return new Users().login(new LoginRequest(user.email(), password));
    }

    @Step("Log out via API")
    private void logoutViaApi() {
        verifyStatus(users.logout(), SC_OK);
    }

    @Step("Delete current user via API")
    private void deleteCurrentUserViaApi() {
        verifyStatus(users.deleteCurrentUser(), SC_OK);
    }

    // ---------- Verifications ----------

    @Step("Verify profile of the registered user")
    private void verifyProfileOfRegisteredUser(UserResponse profile) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(profile.id()).as("id").isEqualTo(auth.user().id());
            softly.assertThat(profile.firstName()).as("first name").isEqualTo(user.firstName());
            softly.assertThat(profile.lastName()).as("last name").isEqualTo(user.lastName());
            softly.assertThat(profile.email()).as("email").isEqualTo(user.email());
        });
    }

    @Step("Verify user is logged in")
    private void verifyLoggedInAsRegisteredUser(Response response) {
        verifyStatus(response, SC_OK);
        AuthResponse login = response.as(AuthResponse.class);
        assertThat(login.token()).as("token").isNotBlank();
        assertThat(login.user().id()).as("logged in user id").isEqualTo(auth.user().id());
    }

    @Step("Verify token is rejected")
    private void verifyTokenIsRejected() {
        verifyStatus(users.getProfile(), SC_UNAUTHORIZED);
    }

    @Step("Verify user cannot log in")
    private void verifyUserCannotLogIn() {
        verifyStatus(loginViaApi(user.password()), SC_UNAUTHORIZED);
    }
}
