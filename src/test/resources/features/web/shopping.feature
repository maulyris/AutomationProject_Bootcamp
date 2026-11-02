@web @shopping
Feature: Demoblaze - Browse products, cart and checkout
  As a shopper
  I want to browse products, manage my cart and place an order
  So that I can buy items

  Background:
    Given I am on the Demoblaze home page

  @smoke
  Scenario Outline: Filter products by category
    When I select the "<category>" category
    Then the product list should contain "<product>"

    Examples:
      | category | product           |
      | Phones   | Samsung galaxy s6 |
      | Laptops  | MacBook Pro       |
      | Monitors | Apple monitor 24  |

  @cart @smoke
  Scenario: Add a product to the cart
    When I add the product "Samsung galaxy s6" to the cart
    Then I should see the alert "Product added"
    When I open the cart page
    Then the cart should contain the added products
    And the cart total should equal the sum of the added product prices

  @cart
  Scenario: Add multiple products and verify the cart total
    Given I have added the product "Nokia lumia 1520" to the cart
    And I have added the product "Sony vaio i5" to the cart
    When I open the cart page
    Then the cart should contain the added products
    And the cart total should equal the sum of the added product prices

  @cart
  Scenario: Remove a product from the cart
    Given I have added the product "Nexus 6" to the cart
    And I have added the product "Iphone 6 32gb" to the cart
    When I open the cart page
    And I delete the product "Nexus 6" from the cart
    Then the cart should not contain "Nexus 6"
    And the cart total should equal the sum of the added product prices

  @checkout @smoke
  Scenario: Place an order successfully
    Given I have added the product "Samsung galaxy s6" to the cart
    When I open the cart page
    And I place an order with valid customer data
    Then I should see the purchase confirmation "Thank you for your purchase!"
    And the purchase confirmation amount should equal the cart total

  @checkout @negative
  Scenario: Place an order without customer name and credit card
    Given I have added the product "Samsung galaxy s6" to the cart
    When I open the cart page
    And I place an order without customer name and credit card
    Then I should see the alert "Please fill out Name and Creditcard."
