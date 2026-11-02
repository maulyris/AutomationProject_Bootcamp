package com.automation.api.utils;

/** Konfigurasi API test. Bisa di-override lewat environment variable. */
public final class ApiConfig {

    private ApiConfig() {
    }

    public static final String BASE_URI = getOrDefault("API_BASE_URI", "https://dummyapi.io/data/v1");

    /** app-id default dari soal; override dengan env APP_ID (mis. GitHub secret). */
    public static final String APP_ID = getOrDefault("APP_ID", "63a804408eb0cb069b57e43a");

    private static String getOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? def : v;
    }
}
