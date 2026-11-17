package io.github.piotrwaligora.e2e.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.piotrwaligora.e2e.api.client.Users;
import io.github.piotrwaligora.e2e.api.model.AuthResponse;
import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.bdd.context.TestContext;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import java.util.Locale;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRegistrationSteps {

    private static final String UNIQUE_PLACEHOLDER = "{unique}";

    private final TestContext context;

    public UserRegistrationSteps(TestContext context) {
        this.context = context;
    }

    @Given("a user with first name {string}, last name {string}, email {string} and password {string}")
    public void aUser(String firstName, String lastName, String emailTemplate, String password) {
        assertThat(emailTemplate).as("email template").contains(UNIQUE_PLACEHOLDER);
        String email = emailTemplate.replace(UNIQUE_PLACEHOLDER, context.getUniqueId());
        context.setUser(new UserRequest(firstName, lastName, email, password));
    }

    @When("the user registers through the API")
    public void theUserRegisters() {
        context.setRegistrationResponse(new Users().register(context.getUser()));
    }

    @Then("the registration succeeds")
    public void theRegistrationSucceeds() {
        Response response = context.getRegistrationResponse();
        assertThat(response.statusCode())
                .as("registration status, body: %s", response.asString())
                .isEqualTo(SC_CREATED);
    }

    @Then("the response contains the registered user and a token")
    public void theResponseContainsTheUserAndToken() {
        UserRequest expected = context.getUser();
        AuthResponse actual = context.getRegistrationResponse().as(AuthResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.user().id()).as("id").isNotBlank();
            softly.assertThat(actual.user().firstName()).as("first name").isEqualTo(expected.firstName());
            softly.assertThat(actual.user().lastName()).as("last name").isEqualTo(expected.lastName());
            softly.assertThat(actual.user().email()).as("email")
                    .isEqualTo(expected.email().toLowerCase(Locale.ROOT));
            softly.assertThat(actual.token()).as("token").isNotBlank();
        });
    }

    @Then("the user can log in with the registered credentials")
    public void theUserCanLogIn() {
        UserRequest user = context.getUser();
        String registeredId = context.getRegistrationResponse().as(AuthResponse.class).user().id();

        Response login = new Users().login(new LoginRequest(user.email(), user.password()));

        assertThat(login.statusCode()).as("login status").isEqualTo(SC_OK);
        assertThat(login.as(AuthResponse.class).user().id()).as("logged in user id").isEqualTo(registeredId);
    }
}
