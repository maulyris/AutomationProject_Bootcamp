package com.automation.api.steps;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/** State yang dibagikan antar step (di-inject PicoContainer, 1 instance per scenario). */
public class ApiContext {
    public Response response;
    public String userId;
    public Map<String, Object> requestBody = new HashMap<>();
}
