package com.automation.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Pembuat test data dinamis (email DummyAPI harus unik). */
public final class UserDataFactory {

    private UserDataFactory() {
    }

    public static Map<String, Object> newUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Maulida");
        body.put("lastName", "Tester");
        body.put("email", "maulida." + suffix + "@example.com");
        return body;
    }
}
