package io.github.piotrwaligora.e2e.data;

import io.github.piotrwaligora.e2e.api.model.ContactRequest;
import io.github.piotrwaligora.e2e.api.model.UserRequest;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/** Creates unique test data so that tests never share users or contacts. */
public final class TestDataFactory {

    private static final String EMAIL_DOMAIN = "example.com";

    private TestDataFactory() {
    }

    public static UserRequest newUser() {
        String id = uniqueId();
        return new UserRequest("Test", "User " + id, "user." + id + "@" + EMAIL_DOMAIN, "Pass-" + id);
    }

    public static ContactRequest newContact() {
        String id = uniqueId();
        return new ContactRequest(
                "John",
                "Doe " + id,
                "1990-05-15",
                "contact." + id + "@" + EMAIL_DOMAIN,
                randomDigits(10),
                "1 Main St.",
                "Apartment " + id,
                "Anytown",
                "KS",
                randomDigits(5),
                "USA");
    }

    private static String uniqueId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private static String randomDigits(int length) {
        StringBuilder digits = new StringBuilder(length);
        digits.append(ThreadLocalRandom.current().nextInt(1, 10));
        for (int i = 1; i < length; i++) {
            digits.append(ThreadLocalRandom.current().nextInt(10));
        }
        return digits.toString();
    }
}
