@web @auth
Feature: Demoblaze - Sign up and login
  As a visitor of Demoblaze
  I want to register and log in
  So that I can shop with my own account

  Background:
    Given I am on the Demoblaze home page

  @smoke
  Scenario: Sign up with a new user and login successfully
    When I sign up with a new unique username and password
    Then I should see the alert "Sign up successful."
    When I log in with the registered credentials
    Then I should be logged in as the registered user

  @smoke
  Scenario: Logout after login
    Given a registered user exists
    When I log in with the registered credentials
    Then I should be logged in as the registered user
    When I log out
    Then I should be logged out

  @negative
  Scenario: Sign up with an already registered username
    Given a registered user exists
    When I sign up again with the same username
    Then I should see the alert "This user already exist."

  @negative
  Scenario: Login with wrong password
    Given a registered user exists
    When I log in with the registered username and password "wrong-password"
    Then I should see the alert "Wrong password."

  @negative
  Scenario: Login with unregistered user
    When I log in with username "not_exist_user_zzz999" and password "whatever"
    Then I should see the alert "User does not exist."

  @negative
  Scenario: Login with empty username and password
    When I log in with username "" and password ""
    Then I should see the alert "Please fill out Username and Password."
