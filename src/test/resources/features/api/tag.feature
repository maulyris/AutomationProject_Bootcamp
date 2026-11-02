@api @tag
Feature: DummyAPI - Tag
  As an API consumer
  I want to get the list of tags
  So that I can use them to filter posts

  @smoke
  Scenario: Get list of tags
    When I request the list of tags
    Then the response status code should be 200
    And the response content type should be JSON
    And the response should match schema "tag-list-schema.json"
    And the tag list should not be empty
    And every tag in the list should be a non-empty string
    # Endpoint /tag di DummyAPI hanya mengembalikan "data" (tanpa total/page/limit), jadi tidak ada cek pagination

  @negative
  Scenario: Get list of tags without app-id header
    When I request the list of tags without app-id header
    Then the response status code should be 403
    And the response error should be "APP_ID_MISSING"
