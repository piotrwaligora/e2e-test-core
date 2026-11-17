package io.github.piotrwaligora.e2e.bdd.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.piotrwaligora.e2e.api.model.UserRequest;
import io.github.piotrwaligora.e2e.bdd.context.TestContext;
import io.github.piotrwaligora.e2e.data.TestDataCleaner;

import java.util.UUID;

public class ApiHooks {

    private final TestContext context;

    public ApiHooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void prepareContext() {
        context.setUniqueId(UUID.randomUUID().toString().substring(0, 8));
    }

    /** Runs also when the scenario fails. */
    @After
    public void deleteTestData() {
        UserRequest user = context.getUser();
        if (user != null) {
            TestDataCleaner.deleteUser(user.email(), user.password());
        }
    }
}
