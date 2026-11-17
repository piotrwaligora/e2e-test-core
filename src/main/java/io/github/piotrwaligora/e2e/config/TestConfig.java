package io.github.piotrwaligora.e2e.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Read-only test configuration loaded from {@code config.properties}.
 * JVM system properties take precedence over values from the file.
 */
public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration property: " + key);
        }
        return value.trim();
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found on the classpath");
            }
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + CONFIG_FILE, e);
        }
    }
}
