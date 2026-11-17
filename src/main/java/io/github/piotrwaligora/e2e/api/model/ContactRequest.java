package io.github.piotrwaligora.e2e.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContactRequest(
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
        String country) {

    public ContactRequest withFirstName(String newFirstName) {
        return new ContactRequest(newFirstName, lastName, birthdate, email, phone,
                street1, street2, city, stateProvince, postalCode, country);
    }

    public ContactRequest withLastName(String newLastName) {
        return new ContactRequest(firstName, newLastName, birthdate, email, phone,
                street1, street2, city, stateProvince, postalCode, country);
    }
}
