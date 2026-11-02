package com.automation.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Halaman detail produk. */
public class ProductPage extends BasePage {

    private static final By NAME = By.cssSelector("h2.name");
    private static final By PRICE = By.cssSelector("h3.price-container");
    private static final By ADD_TO_CART = By.xpath("//a[contains(@class,'btn-success') and normalize-space()='Add to cart']");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String name() {
        return textOf(NAME);
    }

    /** Harga numerik, mis. "$360 *includes tax" -> 360. */
    public int price() {
        return Integer.parseInt(textOf(PRICE).replaceAll("^\\$(\\d+).*$", "$1"));
    }

    /** @return teks alert setelah klik Add to cart. */
    public String addToCart() {
        click(ADD_TO_CART);
        return acceptAlert();
    }
}
