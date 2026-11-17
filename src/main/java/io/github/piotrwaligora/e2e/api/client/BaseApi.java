package io.github.piotrwaligora.e2e.api.client;

import io.github.piotrwaligora.e2e.config.TestConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Builds request specifications shared by all API clients.
 * A client created without a token sends unauthenticated requests.
 * Every request and response is attached to the Allure report.
 */
public abstract class BaseApi {

    private static final String AUTHORIZATION = "Authorization";
    private static final String SET_COOKIE = "Set-Cookie";

    // Blacklisted headers are masked in logs and in Allure attachments.
    private static final RestAssuredConfig CONFIG = RestAssured.config()
            .logConfig(LogConfig.logConfig()
                    .blacklistHeader(AUTHORIZATION, SET_COOKIE));

    private static final AllureRestAssured ALLURE_FILTER = new AllureRestAssured();

    private final String token;

    protected BaseApi(String token) {
        this.token = token;
    }

    protected RequestSpecification request() {
        RequestSpecification spec = RestAssured.given()
                .config(CONFIG)
                .filter(ALLURE_FILTER)
                .baseUri(TestConfig.baseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        if (token != null) {
            spec.header(AUTHORIZATION, "Bearer " + token);
        }
        return spec;
    }
}
