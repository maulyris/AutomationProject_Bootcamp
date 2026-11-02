package com.automation.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Halaman utama Demoblaze: navbar, kategori, daftar produk. */
public class HomePage extends BasePage {

    private static final By NAV_LOGIN = By.id("login2");
    private static final By NAV_SIGN_UP = By.id("signin2");
    private static final By NAV_LOGOUT = By.id("logout2");
    private static final By NAV_CART = By.id("cartur");
    private static final By WELCOME_USER = By.id("nameofuser");
    private static final By PRODUCT_TITLES = By.cssSelector("#tbodyid .card-title a");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open(String url) {
        driver.get(url);
        allVisible(PRODUCT_TITLES);
        return this;
    }

    public void clickLogin() {
        click(NAV_LOGIN);
    }

    /** Membuka modal Sign up; klik diulang sekali jika modal belum muncul (animasi modal sebelumnya / CI lambat). */
    public void clickSignUp() {
        waitInvisible(By.cssSelector(".modal-backdrop"));
        click(NAV_SIGN_UP);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("sign-username")));
        } catch (TimeoutException e) {
            click(NAV_SIGN_UP);
        }
    }

    public void clickLogout() {
        click(NAV_LOGOUT);
    }

    public void clickCart() {
        click(NAV_CART);
    }

    public String welcomeText() {
        return textOf(WELCOME_USER);
    }

    public boolean isLogoutVisible() {
        return visible(NAV_LOGOUT).isDisplayed();
    }

    public boolean isLoginLinkVisible() {
        return visible(NAV_LOGIN).isDisplayed();
    }

    public void selectCategory(String category) {
        WebElement oldFirst = allVisible(PRODUCT_TITLES).get(0);
        click(By.xpath("//a[@id='itemc' and normalize-space()='" + category + "']"));
        wait.until(ExpectedConditions.stalenessOf(oldFirst));
        allVisible(PRODUCT_TITLES);
    }

    public List<String> productNames() {
        return allVisible(PRODUCT_TITLES).stream().map(WebElement::getText).map(String::trim).toList();
    }

    public void openProduct(String name) {
        click(By.xpath("//div[@id='tbodyid']//a[@class='hrefch' and normalize-space()='" + name + "']"));
    }
}
