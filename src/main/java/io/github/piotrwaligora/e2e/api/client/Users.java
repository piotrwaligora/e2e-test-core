package io.github.piotrwaligora.e2e.api.client;

import io.github.piotrwaligora.e2e.api.model.LoginRequest;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.restassured.response.Response;

public class Users extends BaseApi {

    private static final String USERS = "/users";
    private static final String CURRENT_USER = "/users/me";

    /** Creates a client for unauthenticated calls, e.g. registration and login. */
    public Users() {
        super(null);
    }

    public Users(String token) {
        super(token);
    }

    public Response register(UserRequest user) {
        return request().body(user).post(USERS);
    }

    public Response login(LoginRequest credentials) {
        return request().body(credentials).post(USERS + "/login");
    }

    public Response getProfile() {
        return request().get(CURRENT_USER);
    }

    /** Note: the API invalidates every token of the user, not only the current one. */
    public Response logout() {
        return request().post(USERS + "/logout");
    }

    public Response deleteCurrentUser() {
        return request().delete(CURRENT_USER);
    }
}
