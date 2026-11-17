package io.github.piotrwaligora.e2e.bdd.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs Cucumber scenarios as TestNG tests.
 * The TestNG integration is deprecated in Cucumber 8 but fully functional; kept on purpose
 * to run all tests with a single engine.
 */
@SuppressWarnings("removal")
@CucumberOptions(
        features = "classpath:features",
        glue = "io.github.piotrwaligora.e2e.bdd",
        plugin = {"summary", "io.qameta.allure.cucumber8jvm.AllureCucumber8Jvm"},
        monochrome = true)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
