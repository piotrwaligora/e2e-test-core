package io.github.piotrwaligora.e2e.api;

import io.github.piotrwaligora.e2e.api.client.Contacts;
import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertNotNull;

public class ContactApiTest extends BaseApiTest {

    @Test(description = "Contact is created with all submitted fields")
    public void shouldCreateContact() {
        ContactRequest contact = TestDataFactory.newContact();

        ContactResponse created = contacts.create(contact)
                .then().statusCode(SC_CREATED)
                .extract().as(ContactResponse.class);

        assertNotNull(created.id());
        assertEquals(created.owner(), auth.user().id());
        assertEquals(created.toRequest(), contact);
    }

    @Test(description = "Contact list returns every contact of the user")
    public void shouldListAllContacts() {
        String firstId = createContact(TestDataFactory.newContact()).id();
        String secondId = createContact(TestDataFactory.newContact()).id();

        List<String> ids = contacts.getAll()
                .then().statusCode(SC_OK)
                .extract().jsonPath().getList("_id", String.class);

        assertEqualsNoOrder(ids, List.of(firstId, secondId));
    }

    @Test(description = "Contact can be read by id")
    public void shouldGetContactById() {
        ContactRequest contact = TestDataFactory.newContact();
        String id = createContact(contact).id();

        ContactResponse fetched = fetchContact(id);

        assertEquals(fetched.id(), id);
        assertEquals(fetched.toRequest(), contact);
    }

    @Test(description = "PUT replaces all contact fields")
    public void shouldUpdateContactWithPut() {
        String id = createContact(TestDataFactory.newContact()).id();
        ContactRequest replacement = TestDataFactory.newContact();

        contacts.update(id, replacement).then().statusCode(SC_OK);

        assertEquals(fetchContact(id).toRequest(), replacement);
    }

    @Test(description = "PATCH changes only the submitted fields")
    public void shouldPartiallyUpdateContactWithPatch() {
        ContactRequest contact = TestDataFactory.newContact();
        String id = createContact(contact).id();

        contacts.patch(id, Map.of("firstName", "Anna")).then().statusCode(SC_OK);

        assertEquals(fetchContact(id).toRequest(), contact.withFirstName("Anna"));
    }

    @Test(description = "Deleted contact is no longer available")
    public void shouldDeleteContact() {
        String id = createContact(TestDataFactory.newContact()).id();

        contacts.delete(id).then().statusCode(SC_OK);

        contacts.getById(id).then().statusCode(SC_NOT_FOUND);
    }

    @Test(description = "Contact without the required last name is rejected")
    public void shouldRejectContactWithoutLastName() {
        ContactRequest invalid = TestDataFactory.newContact().withLastName(null);

        contacts.create(invalid).then().statusCode(SC_BAD_REQUEST);
    }

    @Test(description = "Contacts cannot be read without a token")
    public void shouldRejectRequestWithoutToken() {
        new Contacts().getAll().then().statusCode(SC_UNAUTHORIZED);
    }

    private ContactResponse createContact(ContactRequest contact) {
        return contacts.create(contact)
                .then().statusCode(SC_CREATED)
                .extract().as(ContactResponse.class);
    }

    private ContactResponse fetchContact(String id) {
        return contacts.getById(id)
                .then().statusCode(SC_OK)
                .extract().as(ContactResponse.class);
    }
}
