package io.github.piotrwaligora.e2e.listeners;

import io.github.piotrwaligora.e2e.config.TestConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test according to {@code retry.count} (0 = disabled, maximum 1).
 * TestNG creates a new analyzer for every test method invocation, so each test,
 * including every Cucumber scenario, has its own attempt counter.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 1;

    private final int maxRetries = Math.clamp(TestConfig.retryCount(), 0, MAX_RETRIES);
    private int retries;

    @Override
    public boolean retry(ITestResult result) {
        if (retries < maxRetries) {
            retries++;
            return true;
        }
        return false;
    }
}
