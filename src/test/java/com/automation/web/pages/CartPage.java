package com.automation.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Halaman Cart + modal Place Order. */
public class CartPage extends BasePage {

    private static final By ROWS = By.cssSelector("#tbodyid tr.success");
    private static final By ROW_TITLES = By.cssSelector("#tbodyid tr.success td:nth-child(2)");
    private static final By ROW_PRICES = By.cssSelector("#tbodyid tr.success td:nth-child(3)");
    private static final By DELETE_LINKS = By.cssSelector("#tbodyid tr.success td:nth-child(4) a");
    private static final By TOTAL = By.id("totalp");
    private static final By PLACE_ORDER = By.cssSelector("button[data-target='#orderModal']");

    private static final By ORDER_NAME = By.id("name");
    private static final By ORDER_COUNTRY = By.id("country");
    private static final By ORDER_CITY = By.id("city");
    private static final By ORDER_CARD = By.id("card");
    private static final By ORDER_MONTH = By.id("month");
    private static final By ORDER_YEAR = By.id("year");
    private static final By PURCHASE = By.cssSelector("#orderModal button[onclick='purchaseOrder()']");

    private static final By SUCCESS_TITLE = By.cssSelector(".sweet-alert h2");
    private static final By SUCCESS_DETAIL = By.cssSelector(".sweet-alert p.text-muted");
    private static final By SUCCESS_OK = By.cssSelector(".sweet-alert button.confirm");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /** Menunggu minimal 1 item muncul (data cart di-load via AJAX). */
    public CartPage waitForItems() {
        allVisible(ROWS);
        return this;
    }

    public List<String> itemTitles() {
        return allVisible(ROW_TITLES).stream().map(e -> e.getText().trim()).toList();
    }

    public List<Integer> itemPrices() {
        return allVisible(ROW_PRICES).stream().map(e -> Integer.parseInt(e.getText().trim())).toList();
    }

    /** Total di cart di-update via AJAX, jadi tunggu sampai sesuai nilai yang diharapkan. */
    public int totalAfterUpdate(int expected) {
        try {
            wait.until(d -> d.findElement(TOTAL).getText().trim().equals(String.valueOf(expected)));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // biarkan assertion di step yang melaporkan nilai aktual
        }
        String t = driver.findElement(TOTAL).getText().trim();
        return t.isEmpty() ? 0 : Integer.parseInt(t);
    }

    public void deleteItem(String title) {
        By row = By.xpath("//tr[@class='success'][td[2][normalize-space()='" + title + "']]");
        WebElement rowEl = visible(row);
        rowEl.findElement(By.xpath("./td[4]/a")).click();
        wait.until(ExpectedConditions.stalenessOf(rowEl));
    }

    public boolean isEmpty() {
        return wait.until(d -> d.findElements(ROWS).isEmpty());
    }

    public void clickPlaceOrder() {
        click(PLACE_ORDER);
    }

    public void fillOrderForm(String name, String country, String city, String card, String month, String year) {
        type(ORDER_NAME, name);
        type(ORDER_COUNTRY, country);
        type(ORDER_CITY, city);
        type(ORDER_CARD, card);
        type(ORDER_MONTH, month);
        type(ORDER_YEAR, year);
    }

    public void clickPurchase() {
        click(PURCHASE);
    }

    public String successTitle() {
        return textOf(SUCCESS_TITLE);
    }

    public String successDetail() {
        return textOf(SUCCESS_DETAIL);
    }

    public void confirmSuccess() {
        click(SUCCESS_OK);
    }
}
