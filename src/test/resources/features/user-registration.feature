Feature: User registration via API
  As a new user
  I want to create an account through the REST API
  So that I can manage my contacts

  Every example runs as an independent scenario with its own user.
  "{unique}" in the email is replaced with a value unique for the scenario.
  The API stores email addresses in lower case.

  Scenario Outline: Register a new user - <case>
    Given a user with first name "<firstName>", last name "<lastName>", email "<email>" and password "<password>"
    When the user registers through the API
    Then the registration succeeds
    And the response contains the registered user and a token
    And the user can log in with the registered credentials

    Examples:
      | case                    | firstName            | lastName             | email                               | password                     |
      | typical user            | John                 | Doe                  | john.doe.{unique}@example.com       | Secret123                    |
      | mixed case email        | Anna                 | Kowalska             | Anna.Kowalska.{unique}@Example.COM  | Haslo-2024                   |
      | diacritics in names     | Zoë                  | Łukasiewicz-Żółć     | zoe.{unique}@example.com            | Pässwörd1                    |
      | apostrophe and space    | O'Brien              | Mc Donald            | obrien_{unique}@example.co.uk       | Pa$$w0rd!                    |
      | short names             | Li                   | Wu                   | li.{unique}@example.com             | Secret123                    |
      | minimum password length | Mia                  | Novak                | mia.{unique}@example.com            | 1234567                      |
      | maximum name length     | Maximilian-Alexander | Montgomery-Smithsons | max.{unique}@example.com            | Secret123                    |
      | plus addressing         | Eva                  | Green                | eva+newsletter.{unique}@example.com | Secret123                    |
      | hyphenated domain       | Tom                  | Hardy                | tom-{unique}@test-mail.com          | Secret123                    |
      | passphrase password     | Jean-Luc             | Picard               | jean.luc.{unique}@example.org       | correct horse battery staple |
