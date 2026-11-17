package io.github.piotrwaligora.e2e.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthResponse(UserResponse user, String token) {
}
