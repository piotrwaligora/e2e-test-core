package io.github.piotrwaligora.e2e.smoke;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SetupSmokeTest {

    private static final int REQUIRED_JAVA_VERSION = 25;

    @Test(description = "Verifies that tests run on the required Java version")
    public void shouldRunOnRequiredJavaVersion() {
        int actualVersion = Runtime.version().feature();

        Assert.assertTrue(actualVersion >= REQUIRED_JAVA_VERSION,
                "Expected Java " + REQUIRED_JAVA_VERSION + " or newer, but was " + actualVersion);
    }
}
