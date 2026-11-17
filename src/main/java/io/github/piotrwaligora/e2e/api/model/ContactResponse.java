package io.github.piotrwaligora.e2e.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContactResponse(
        @JsonProperty("_id") String id,
        String firstName,
        String lastName,
        String birthdate,
        String email,
        String phone,
        String street1,
        String street2,
        String city,
        String stateProvince,
        String postalCode,
        String country,
        String owner) {

    /** Returns the contact data without server-generated fields, for comparison with a request. */
    public ContactRequest toRequest() {
        return new ContactRequest(firstName, lastName, birthdate, email, phone,
                street1, street2, city, stateProvince, postalCode, country);
    }
}
