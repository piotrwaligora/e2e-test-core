Feature: Contact management via API
  As a registered user
  I want to add and update my contacts through the REST API
  So that my contact list stays up to date

  Background:
    Given a registered user

  Scenario: Add a new contact
    When the user adds a contact with details:
      | firstName     | Jane             |
      | lastName      | Smith            |
      | birthdate     | 1985-03-21       |
      | email         | jane@example.com |
      | phone         | 8005551234       |
      | street1       | 5 Oak Avenue     |
      | street2       | Suite 12         |
      | city          | Boston           |
      | stateProvince | MA               |
      | postalCode    | 02108            |
      | country       | USA              |
    Then the contact is saved with the submitted details
    And the contact list contains 1 contact

  Scenario: Update an existing contact
    Given the user has a contact
    When the user updates the contact with:
      | city          | Krakow      |
      | stateProvince | Malopolskie |
      | postalCode    | 30-001      |
      | country       | Poland      |
    Then the contact has the updated details
