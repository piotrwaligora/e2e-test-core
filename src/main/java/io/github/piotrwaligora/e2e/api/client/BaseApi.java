package io.github.piotrwaligora.e2e.api.client;

import io.github.piotrwaligora.e2e.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Builds request specifications shared by all API clients.
 * A client created without a token sends unauthenticated requests.
 */
public abstract class BaseApi {

    private static final String AUTHORIZATION = "Authorization";

    private static final RestAssuredConfig CONFIG = RestAssured.config()
            .logConfig(LogConfig.logConfig()
                    .enableLoggingOfRequestAndResponseIfValidationFails()
                    .blacklistHeader(AUTHORIZATION));

    private final String token;

    protected BaseApi(String token) {
        this.token = token;
    }

    protected RequestSpecification request() {
        RequestSpecification spec = RestAssured.given()
                .config(CONFIG)
                .baseUri(TestConfig.baseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        if (token != null) {
            spec.header(AUTHORIZATION, "Bearer " + token);
        }
        return spec;
    }
}
