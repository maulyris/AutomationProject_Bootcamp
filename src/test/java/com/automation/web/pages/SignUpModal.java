package com.automation.web.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Modal "Sign up". */
public class SignUpModal extends BasePage {

    private static final By USERNAME = By.id("sign-username");
    private static final By PASSWORD = By.id("sign-password");
    private static final By SUBMIT = By.cssSelector("#signInModal button[onclick='register()']");

    public SignUpModal(WebDriver driver) {
        super(driver);
    }

    /** @return teks alert hasil sign up. */
    public String signUp(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(SUBMIT);
        return acceptAlert();
    }

    public void waitUntilClosed() {
        waitInvisible(USERNAME);
    }
}
