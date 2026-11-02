@api @user
Feature: DummyAPI - User management
  As an API consumer
  I want to manage users through DummyAPI
  So that I can get, create, update and delete user data

  @get-user @smoke
  Scenario: Get user by valid ID
    Given a user exists in DummyAPI
    When I send a request to get the user by that id
    Then the response status code should be 200
    And the response content type should be JSON
    And the response should match schema "user-schema.json"
    And the response user id should equal the created user id
    And the response user data should match the request data

  @get-user @negative
  Scenario: Get user with non-existent ID
    When I send a request to get a user by id "0123456789abcdef01234567"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  @get-user @negative
  Scenario: Get user with invalid ID format
    When I send a request to get a user by id "invalid-id"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

  @create-user @smoke
  Scenario: Create user with valid data
    When I create a new user with valid data
    Then the response status code should be 200
    And the response should match schema "user-schema.json"
    And the response user id should not be empty
    And the response user data should match the request data
    And the response should have registerDate and updatedDate

  @create-user @negative
  Scenario: Create user without required email
    When I create a new user without the email field
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"
    And the response body data should contain validation error for "email"

  @update-user @smoke
  Scenario: Update user first name and last name
    Given a user exists in DummyAPI
    When I update that user with first name "Updated" and last name "Name"
    Then the response status code should be 200
    And the response should match schema "user-schema.json"
    And the response user id should equal the created user id
    And the response user data should match the request data
    And the response updatedDate should be later than registerDate

  @update-user @negative
  Scenario: Update user with non-existent ID
    # DummyAPI membalas 400 BODY_NOT_VALID (bukan 404) untuk PUT ke ID yang tidak ada
    When I send a request to update a user with id "0123456789abcdef01234567"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

  @delete-user @smoke
  Scenario: Delete existing user
    Given a user exists in DummyAPI
    When I delete that user
    Then the response status code should be 200
    And the response should match schema "delete-user-schema.json"
    And the deleted user id in response should equal the created user id
    And getting that user again should return status code 404

  @delete-user @negative
  Scenario: Delete user with non-existent ID
    When I delete a user with id "0123456789abcdef01234567"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"
