package com.automation.web.steps;

import com.automation.web.pages.*;
import org.openqa.selenium.WebDriver;

import java.util.LinkedHashMap;
import java.util.Map;

/** State per-scenario (di-inject PicoContainer): driver, page objects, dan data test. */
public class WebWorld {

    public WebDriver driver;

    public String username;
    public String password;
    public String lastAlert;
    /** Produk yang sudah ditambahkan ke cart: nama -> harga. */
    public final Map<String, Integer> addedProducts = new LinkedHashMap<>();

    public HomePage home() {
        return new HomePage(driver);
    }

    public LoginModal loginModal() {
        return new LoginModal(driver);
    }

    public SignUpModal signUpModal() {
        return new SignUpModal(driver);
    }

    public ProductPage productPage() {
        return new ProductPage(driver);
    }

    public CartPage cartPage() {
        return new CartPage(driver);
    }
}
