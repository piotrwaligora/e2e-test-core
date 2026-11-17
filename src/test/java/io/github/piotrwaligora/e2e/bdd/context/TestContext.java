package io.github.piotrwaligora.e2e.bdd.context;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
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
    private Contacts contacts;
    private ContactResponse contact;
    private ContactRequest expectedContact;

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

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public ContactResponse getContact() {
        return contact;
    }

    public void setContact(ContactResponse contact) {
        this.contact = contact;
    }

    public ContactRequest getExpectedContact() {
        return expectedContact;
    }

    public void setExpectedContact(ContactRequest expectedContact) {
        this.expectedContact = expectedContact;
    }
}
