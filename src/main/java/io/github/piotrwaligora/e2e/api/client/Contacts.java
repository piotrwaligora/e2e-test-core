package io.github.piotrwaligora.e2e.api.client;

import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.restassured.response.Response;

import java.util.Map;

public class Contacts extends BaseApi {

    private static final String CONTACTS = "/contacts";
    private static final String CONTACT_BY_ID = "/contacts/{id}";

    /** Creates a client for unauthenticated calls, used to verify authorization. */
    public Contacts() {
        super(null);
    }

    public Contacts(String token) {
        super(token);
    }

    public Response create(ContactRequest contact) {
        return request().body(contact).post(CONTACTS);
    }

    public Response getAll() {
        return request().get(CONTACTS);
    }

    public Response getById(String id) {
        return request().get(CONTACT_BY_ID, id);
    }

    public Response update(String id, ContactRequest contact) {
        return request().body(contact).put(CONTACT_BY_ID, id);
    }

    public Response patch(String id, Map<String, Object> fields) {
        return request().body(fields).patch(CONTACT_BY_ID, id);
    }

    public Response delete(String id) {
        return request().delete(CONTACT_BY_ID, id);
    }
}
