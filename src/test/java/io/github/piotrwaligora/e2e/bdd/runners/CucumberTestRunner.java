package io.github.piotrwaligora.e2e.bdd.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = "io.github.piotrwaligora.e2e.bdd",
        plugin = {"pretty", "summary"})
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
