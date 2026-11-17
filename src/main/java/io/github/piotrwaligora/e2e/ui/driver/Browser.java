package io.github.piotrwaligora.e2e.ui.driver;

import java.util.Arrays;

public enum Browser {
    CHROME;

    public static Browser from(String name) {
        return Arrays.stream(values())
                .filter(browser -> browser.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser: " + name + ". Supported: " + Arrays.toString(values())));
    }
}
