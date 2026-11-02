package com.automation.web.steps;

import com.automation.web.utils.WebConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingSteps {

    private final WebWorld world;

    public ShoppingSteps(WebWorld world) {
        this.world = world;
    }

    // ---------- Browse ----------

    @When("I select the {string} category")
    public void selectCategory(String category) {
        world.home().selectCategory(category);
    }

    @Then("the product list should contain {string}")
    public void productListContains(String product) {
        assertThat(world.home().productNames()).contains(product);
    }

    // ---------- Cart ----------

    @Given("I have added the product {string} to the cart")
    @When("I add the product {string} to the cart")
    public void addProduct(String name) {
        if (!world.addedProducts.isEmpty()) {
            world.home().open(WebConfig.baseUrl());
        }
        world.home().openProduct(name);
        assertThat(world.productPage().name()).isEqualTo(name);
        int price = world.productPage().price();
        world.lastAlert = world.productPage().addToCart();
        world.addedProducts.put(name, price);
    }

    @When("I open the cart page")
    public void openCart() {
        world.home().clickCart();
        world.cartPage().waitForItems();
    }

    @When("I delete the product {string} from the cart")
    public void deleteProduct(String name) {
        world.cartPage().deleteItem(name);
        world.addedProducts.remove(name);
    }

    @Then("the cart should contain the added products")
    public void cartContainsAdded() {
        List<String> titles = world.cartPage().itemTitles();
        assertThat(titles).containsExactlyInAnyOrderElementsOf(world.addedProducts.keySet());
    }

    @Then("the cart should not contain {string}")
    public void cartNotContains(String name) {
        assertThat(world.cartPage().itemTitles()).doesNotContain(name);
    }

    @Then("the cart should be empty")
    public void cartEmpty() {
        assertThat(world.cartPage().isEmpty()).isTrue();
    }

    @Then("the cart total should equal the sum of the added product prices")
    public void cartTotal() {
        int expected = world.addedProducts.values().stream().mapToInt(Integer::intValue).sum();
        assertThat(world.cartPage().itemPrices()).containsExactlyInAnyOrderElementsOf(world.addedProducts.values());
        // (harga per-item juga divalidasi terhadap harga di halaman produk)
        assertThat(world.cartPage().totalAfterUpdate(expected)).isEqualTo(expected);
    }

    // ---------- Checkout ----------

    @When("I place an order with valid customer data")
    public void placeOrderValid() {
        world.cartPage().clickPlaceOrder();
        world.cartPage().fillOrderForm("Maulida Tester", "Indonesia", "Jakarta",
                "4111111111111111", "12", "2030");
        world.cartPage().clickPurchase();
    }

    @When("I place an order without customer name and credit card")
    public void placeOrderEmpty() {
        world.lastAlert = null; // buang alert lama (mis. "Product added") agar yang dibaca alert checkout
        world.cartPage().clickPlaceOrder();
        world.cartPage().clickPurchase();
    }

    @Then("I should see the purchase confirmation {string}")
    public void purchaseConfirmation(String message) {
        assertThat(world.cartPage().successTitle()).isEqualTo(message);
    }

    @Then("the purchase confirmation amount should equal the cart total")
    public void confirmationAmount() {
        int expected = world.addedProducts.values().stream().mapToInt(Integer::intValue).sum();
        assertThat(world.cartPage().successDetail()).contains("Amount: " + expected + " USD");
    }
}
