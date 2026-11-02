package com.automation.web.utils;

/** Konfigurasi Web test. Prioritas: system property (-Pxxx via Gradle) > env var > default. */
public final class WebConfig {

    private WebConfig() {
    }

    public static String baseUrl() {
        return get("baseUrl", "BASE_URL", "https://www.demoblaze.com/");
    }

    public static String browser() {
        return get("browser", "BROWSER", "chrome").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "HEADLESS", "false"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeout", "TIMEOUT", "15"));
    }

    private static String get(String prop, String env, String def) {
        String v = System.getProperty(prop);
        if (v == null || v.isBlank()) {
            v = System.getenv(env);
        }
        return (v == null || v.isBlank()) ? def : v;
    }
}
