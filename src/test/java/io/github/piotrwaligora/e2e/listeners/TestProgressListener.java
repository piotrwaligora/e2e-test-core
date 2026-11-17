package io.github.piotrwaligora.e2e.listeners;

import io.cucumber.testng.PickleWrapper;
import org.testng.IDataProviderInterceptor;
import org.testng.IDataProviderMethod;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prints test progress to the console, e.g. {@code [API 3/14] STARTED ContactApiTest.shouldGetContactById}.
 * Uses standard output only, so the progress is visible in Maven and CI consoles without a logging library.
 * Counters are kept per {@code <test>} section of testng.xml.
 */
public class TestProgressListener implements ITestListener, IDataProviderInterceptor {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String INDEX = "progressIndex";
    private static final int MAX_REASON_LENGTH = 200;

    private final Map<String, Integer> totals = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    @Override
    public void onStart(ITestContext context) {
        totals.put(context.getName(), context.getAllTestMethods().length);
        counters.put(context.getName(), new AtomicInteger());
        log("===== %s =====", context.getName());
    }

    /** A data provider runs one method many times (e.g. Cucumber scenarios), so the total is adjusted here. */
    @Override
    public Iterator<Object[]> intercept(Iterator<Object[]> original, IDataProviderMethod dataProviderMethod,
                                        ITestNGMethod method, ITestContext context) {
        List<Object[]> rows = new ArrayList<>();
        original.forEachRemaining(rows::add);
        totals.merge(context.getName(), rows.size() - 1, Integer::sum);
        return rows.iterator();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String section = result.getTestContext().getName();
        int index = counters.get(section).incrementAndGet();
        result.setAttribute(INDEX, "[%s %d/%d]".formatted(section, index, totals.get(section)));
        log("%s STARTED %s", result.getAttribute(INDEX), testName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log("%s PASSED  %s (%s)", result.getAttribute(INDEX), testName(result), duration(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log("%s FAILED  %s (%s): %s", result.getAttribute(INDEX), testName(result), duration(result),
                shortReason(result.getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.wasRetried()) {
            // The failed attempt is reported as skipped; the retry reuses the same position.
            counters.get(result.getTestContext().getName()).decrementAndGet();
            log("%s RETRY   %s (%s): %s", result.getAttribute(INDEX), testName(result), duration(result),
                    shortReason(result.getThrowable()));
            return;
        }
        log("%s SKIPPED %s", result.getAttribute(INDEX), testName(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        log("===== %s finished: %d passed, %d failed, %d skipped =====", context.getName(),
                context.getPassedTests().size(), context.getFailedTests().size(), context.getSkippedTests().size());
    }

    private static String testName(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters.length > 0 && parameters[0] instanceof PickleWrapper scenario) {
            return "Scenario: " + scenario.getPickle().getName();
        }
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }

    private static String duration(ITestResult result) {
        return String.format(Locale.ROOT, "%.1f s", (result.getEndMillis() - result.getStartMillis()) / 1000.0);
    }

    /** Failure reason in one line; the full stack trace is still printed by Surefire. */
    private static String shortReason(Throwable throwable) {
        if (throwable == null || throwable.getMessage() == null) {
            return throwable == null ? "" : throwable.getClass().getSimpleName();
        }
        String reason = throwable.getMessage().strip().replaceAll("\\s+", " ");
        return reason.length() > MAX_REASON_LENGTH ? reason.substring(0, MAX_REASON_LENGTH) + "..." : reason;
    }

    private static void log(String format, Object... args) {
        System.out.println(LocalTime.now().format(TIME) + " " + format.formatted(args));
    }
}
