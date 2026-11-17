package io.github.piotrwaligora.e2e.api.model;

public record UserRequest(String firstName, String lastName, String email, String password) {
}
