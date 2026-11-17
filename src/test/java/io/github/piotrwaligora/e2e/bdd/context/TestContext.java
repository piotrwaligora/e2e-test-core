package io.github.piotrwaligora.e2e.bdd.context;

import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.restassured.response.Response;

/**
 * Scenario-scoped state shared between hooks and step definitions.
 * PicoContainer creates a new instance for every scenario.
 */
public class TestContext {

    private String uniqueId;
    private UserRequest user;
    private Response registrationResponse;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UserRequest getUser() {
        return user;
    }

    public void setUser(UserRequest user) {
        this.user = user;
    }

    public Response getRegistrationResponse() {
        return registrationResponse;
    }

    public void setRegistrationResponse(Response registrationResponse) {
        this.registrationResponse = registrationResponse;
    }
}
