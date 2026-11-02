package com.automation.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Modal "Log in". */
public class LoginModal extends BasePage {

    private static final By USERNAME = By.id("loginusername");
    private static final By PASSWORD = By.id("loginpassword");
    private static final By SUBMIT = By.cssSelector("#logInModal button[onclick='logIn()']");

    public LoginModal(WebDriver driver) {
        super(driver);
    }

    public void fillAndSubmit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(SUBMIT);
    }
}
