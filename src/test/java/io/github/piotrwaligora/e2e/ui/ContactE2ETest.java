package io.github.piotrwaligora.e2e.ui;

import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.ContactResponse;
import io.github.piotrwaligora.e2e.data.TestDataFactory;
import io.github.piotrwaligora.e2e.ui.pages.ContactDetailsPage;
import io.github.piotrwaligora.e2e.ui.pages.ContactListPage;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactE2ETest extends BaseUiTest {

    @Test(description = "Contact created through the API is shown in the UI")
    public void shouldShowContactCreatedThroughApi() {
        ContactRequest contact = createContactViaApi().toRequest();

        ContactListPage contactList = loginViaUi();
        verifyContactIsListed(contactList, contact);

        ContactDetailsPage details = contactList.openContact(contact.fullName());
        verifyContactData(details.getContact(), contact);
    }

    @Test(description = "Contact added in the UI is stored and returned by the API")
    public void shouldStoreContactAddedThroughUi() {
        ContactRequest contact = TestDataFactory.newContact();

        ContactListPage contactList = loginWithApiToken()
                .clickAddContact()
                .addContact(contact);
        verifyContactIsListed(contactList, contact);

        verifyContactIsStoredInApi(contact);
    }

    // ---------- Verifications ----------

    private void verifyContactIsListed(ContactListPage contactList, ContactRequest contact) {
        assertThat(contactList.getContactRowText(contact.fullName()))
                .as("contact list row")
                .contains(contact.email(), contact.phone(), contact.country());
    }

    private void verifyContactIsStoredInApi(ContactRequest expected) {
        List<ContactResponse> stored = getAllContactsViaApi();
        assertThat(stored).as("contacts stored in the API").hasSize(1);
        verifyContactBelongsToCurrentUser(stored.getFirst());
        verifyContactData(stored.getFirst().toRequest(), expected);
    }
}
