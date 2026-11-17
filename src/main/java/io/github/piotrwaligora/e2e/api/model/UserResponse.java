package io.github.piotrwaligora.e2e.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
        @JsonProperty("_id") String id,
        String firstName,
        String lastName,
        String email) {
}
