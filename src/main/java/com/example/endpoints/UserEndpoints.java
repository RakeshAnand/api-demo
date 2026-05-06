package com.example.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.example.payloads.LoginRequest;
import com.example.payloads.User;
import com.example.utilities.ConfigReader;

public class UserEndpoints {

    private static final String BASE_URL = ConfigReader.getProperty("BASE_URL");
    private static final String LOGIN_ENDPOINT = ConfigReader.getProperty("LOGIN_PATH");
    private static final String USERS_ENDPOINT = ConfigReader.getProperty("USERS_PATH");

    // Centralized request specification
    private static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-api-key", ConfigReader.getProperty("API_KEY"));
    }

    // --- LOGIN ---
    public static Response login(LoginRequest payload) {
        return getRequestSpec()
                .body(payload)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    // --- USER CREATION ---
    public static Response createUser(User payload) {
        return getRequestSpec()
                .body(payload)
                .when()
                .post(USERS_ENDPOINT);
    }

    // --- USER FETCH ---
    public static Response getUser(String id) {
        return getRequestSpec()
                .pathParam("id", id)
                .when()
                .get(USERS_ENDPOINT + "/{id}");
    }
}
