package com.automation.api.client;

import com.automation.api.utils.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/** API client DummyAPI (mirip konsep Page Object, tapi untuk endpoint). */
public class DummyApiClient {

    private static final String USER = "/user";
    private static final String TAG = "/tag";

    private RequestSpecification spec(String appId) {
        RequestSpecification s = RestAssured.given()
                .baseUri(ApiConfig.BASE_URI)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log().ifValidationFails();
        if (appId != null) {
            s.header("app-id", appId);
        }
        return s;
    }

    public Response getUserById(String id) {
        return spec(ApiConfig.APP_ID).get(USER + "/{id}", id);
    }

    public Response createUser(Map<String, Object> body) {
        return spec(ApiConfig.APP_ID).body(body).post(USER + "/create");
    }

    public Response updateUser(String id, Map<String, Object> body) {
        return spec(ApiConfig.APP_ID).body(body).put(USER + "/{id}", id);
    }

    public Response deleteUser(String id) {
        return spec(ApiConfig.APP_ID).delete(USER + "/{id}", id);
    }

    public Response getTags() {
        return spec(ApiConfig.APP_ID).get(TAG);
    }

    /** Request tanpa header app-id (skenario negatif authentication). */
    public Response getTagsWithoutAppId() {
        return spec(null).get(TAG);
    }
}
