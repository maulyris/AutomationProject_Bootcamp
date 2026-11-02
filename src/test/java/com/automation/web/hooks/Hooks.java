package com.automation.web.hooks;

import com.automation.web.steps.WebWorld;
import com.automation.web.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private final WebWorld world;

    public Hooks(WebWorld world) {
        this.world = world;
    }

    @Before(order = 0)
    public void setUp() {
        world.driver = DriverFactory.create();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        if (world.driver == null) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                byte[] png = ((TakesScreenshot) world.driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", "Screenshot on failure");
            }
        } catch (Exception ignored) {
            // screenshot bisa gagal jika ada alert terbuka; jangan menutupi error asli
        } finally {
            world.driver.quit();
        }
    }
}
