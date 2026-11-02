package com.automation.web.steps;

import com.automation.web.utils.WebConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {

    private final WebWorld world;

    public AuthSteps(WebWorld world) {
        this.world = world;
    }

    @Given("I am on the Demoblaze home page")
    public void openHome() {
        world.home().open(WebConfig.baseUrl());
    }

    @Given("a registered user exists")
    public void registeredUserExists() {
        world.username = "user_" + UUID.randomUUID().toString().substring(0, 8);
        world.password = "Pass_" + UUID.randomUUID().toString().substring(0, 6);
        world.home().clickSignUp();
        String alert = world.signUpModal().signUp(world.username, world.password);
        assertThat(alert).as("precondition: sign up").isEqualTo("Sign up successful.");
        world.signUpModal().waitUntilClosed();
    }

    @When("I sign up with a new unique username and password")
    public void signUpNew() {
        world.username = "user_" + UUID.randomUUID().toString().substring(0, 8);
        world.password = "Pass_" + UUID.randomUUID().toString().substring(0, 6);
        world.home().clickSignUp();
        world.lastAlert = world.signUpModal().signUp(world.username, world.password);
    }

    @When("I sign up again with the same username")
    public void signUpExisting() {
        // Demoblaze kadang gagal membuka modal Sign up untuk kedua kalinya di halaman yang sama,
        // jadi mulai dari halaman yang di-refresh agar state modal bersih.
        world.home().open(WebConfig.baseUrl());
        world.home().clickSignUp();
        world.lastAlert = world.signUpModal().signUp(world.username, world.password);
    }

    @When("I log in with the registered credentials")
    public void loginRegistered() {
        world.home().clickLogin();
        world.loginModal().fillAndSubmit(world.username, world.password);
    }

    @When("I log in with the registered username and password {string}")
    public void loginWrongPassword(String password) {
        world.home().clickLogin();
        world.loginModal().fillAndSubmit(world.username, password);
    }

    @When("I log in with username {string} and password {string}")
    public void loginWith(String username, String password) {
        world.home().clickLogin();
        world.loginModal().fillAndSubmit(username, password);
    }

    @When("I log out")
    public void logout() {
        world.home().clickLogout();
    }

    @Then("I should see the alert {string}")
    public void shouldSeeAlert(String expected) {
        String actual = world.lastAlert != null ? world.lastAlert : world.home().acceptAlert();
        world.lastAlert = null;
        assertThat(actual).isEqualTo(expected);
    }

    @Then("I should be logged in as the registered user")
    public void loggedIn() {
        assertThat(world.home().welcomeText()).isEqualTo("Welcome " + world.username);
        assertThat(world.home().isLogoutVisible()).isTrue();
    }

    @Then("I should be logged out")
    public void loggedOut() {
        assertThat(world.home().isLoginLinkVisible()).isTrue();
    }
}
