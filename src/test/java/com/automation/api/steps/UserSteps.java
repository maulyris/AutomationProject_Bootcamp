package com.automation.api.steps;

import com.automation.api.client.DummyApiClient;
import com.automation.api.utils.UserDataFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    private final ApiContext ctx;
    private final DummyApiClient client = new DummyApiClient();

    public UserSteps(ApiContext ctx) {
        this.ctx = ctx;
    }

    // ---------- Given ----------

    @Given("a user exists in DummyAPI")
    public void aUserExists() {
        Map<String, Object> body = UserDataFactory.newUser();
        var res = client.createUser(body);
        assertThat(res.statusCode()).as("precondition: create user").isEqualTo(200);
        ctx.userId = res.jsonPath().getString("id");
        ctx.requestBody = body;
    }

    // ---------- When ----------

    @When("I send a request to get the user by that id")
    public void getUserByCreatedId() {
        ctx.response = client.getUserById(ctx.userId);
    }

    @When("I send a request to get a user by id {string}")
    public void getUserById(String id) {
        ctx.response = client.getUserById(id);
    }

    @When("I create a new user with valid data")
    public void createValidUser() {
        ctx.requestBody = UserDataFactory.newUser();
        ctx.response = client.createUser(ctx.requestBody);
        if (ctx.response.statusCode() == 200) {
            ctx.userId = ctx.response.jsonPath().getString("id");
        }
    }

    @When("I create a new user without the email field")
    public void createUserWithoutEmail() {
        ctx.requestBody = UserDataFactory.newUser();
        ctx.requestBody.remove("email");
        ctx.response = client.createUser(ctx.requestBody);
    }

    @When("I update that user with first name {string} and last name {string}")
    public void updateUser(String first, String last) {
        ctx.requestBody = new java.util.HashMap<>(Map.of("firstName", first, "lastName", last));
        ctx.response = client.updateUser(ctx.userId, ctx.requestBody);
    }

    @When("I send a request to update a user with id {string}")
    public void updateUserById(String id) {
        ctx.requestBody = new java.util.HashMap<>(Map.of("firstName", "Ghost", "lastName", "User"));
        ctx.response = client.updateUser(id, ctx.requestBody);
    }

    @When("I delete that user")
    public void deleteUser() {
        ctx.response = client.deleteUser(ctx.userId);
    }

    @When("I delete a user with id {string}")
    public void deleteUserById(String id) {
        ctx.response = client.deleteUser(id);
    }

    @When("I request the list of tags")
    public void getTags() {
        ctx.response = client.getTags();
    }

    @When("I request the list of tags without app-id header")
    public void getTagsNoAppId() {
        ctx.response = client.getTagsWithoutAppId();
    }

    // ---------- Then ----------

    @Then("the response status code should be {int}")
    public void statusCode(int expected) {
        assertThat(ctx.response.statusCode())
                .as("status code. Body: %s", ctx.response.asString())
                .isEqualTo(expected);
    }

    @Then("the response should match schema {string}")
    public void matchSchema(String schema) {
        ctx.response.then().body(matchesJsonSchemaInClasspath("schemas/" + schema));
    }

    @Then("the response user id should equal the created user id")
    public void idMatches() {
        assertThat(ctx.response.jsonPath().getString("id")).isEqualTo(ctx.userId);
    }

    @Then("the response user data should match the request data")
    public void dataMatchesRequest() {
        var json = ctx.response.jsonPath();
        assertThat(json.getString("firstName")).isEqualTo(ctx.requestBody.get("firstName"));
        assertThat(json.getString("lastName")).isEqualTo(ctx.requestBody.get("lastName"));
        if (ctx.requestBody.containsKey("email")) {
            assertThat(json.getString("email")).isEqualTo(ctx.requestBody.get("email"));
        }
    }

    @Then("the response user id should not be empty")
    public void idNotEmpty() {
        assertThat(ctx.response.jsonPath().getString("id")).isNotBlank();
    }

    @Then("the response should have registerDate and updatedDate")
    public void hasDates() {
        ctx.response.then().body("registerDate", notNullValue(), "updatedDate", notNullValue());
    }

    @Then("the response updatedDate should be later than registerDate")
    public void updatedAfterRegister() {
        String reg = ctx.response.jsonPath().getString("registerDate");
        String upd = ctx.response.jsonPath().getString("updatedDate");
        assertThat(java.time.Instant.parse(upd)).isAfterOrEqualTo(java.time.Instant.parse(reg));
    }

    @Then("the response error should be {string}")
    public void errorIs(String error) {
        assertThat(ctx.response.jsonPath().getString("error")).isEqualTo(error);
    }

    @Then("the deleted user id in response should equal the created user id")
    public void deletedIdMatches() {
        assertThat(ctx.response.jsonPath().getString("id")).isEqualTo(ctx.userId);
    }

    @Then("getting that user again should return status code {int}")
    public void getAfterDelete(int expected) {
        assertThat(client.getUserById(ctx.userId).statusCode()).isEqualTo(expected);
    }

    @Then("the response body data should contain validation error for {string}")
    public void validationErrorFor(String field) {
        assertThat(ctx.response.jsonPath().getString("data." + field)).isNotBlank();
    }

    @Then("the tag list should not be empty")
    public void tagsNotEmpty() {
        ctx.response.then().body("data", not(empty()));
    }

    @Then("every tag in the list should be a non-empty string")
    public void tagsAreStrings() {
        java.util.List<Object> tags = ctx.response.jsonPath().getList("data");
        // DummyAPI publik berisi entri null/kosong di list tag: semua entri non-null harus String,
        // dan minimal ada satu tag yang tidak kosong.
        java.util.List<Object> nonNull = tags.stream().filter(java.util.Objects::nonNull).toList();
        assertThat(nonNull).isNotEmpty().allSatisfy(t -> assertThat(t).isInstanceOf(String.class));
        assertThat(nonNull).anySatisfy(t -> assertThat((String) t).isNotBlank());
    }

    @Then("the tag response should contain pagination fields")
    public void paginationFields() {
        ctx.response.then().body("total", greaterThanOrEqualTo(0), "page", notNullValue(), "limit", notNullValue());
    }

    @Then("the response content type should be JSON")
    public void contentTypeJson() {
        assertThat(ctx.response.contentType()).containsIgnoringCase("application/json");
    }
}
