package com.example.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.example.payloads.LoginRequest;
import com.example.payloads.User;
import com.example.utilities.ConfigReader;

import static io.restassured.RestAssured.given;

public class UserEndpoints {

    // --- Centralized request specification ---
    private static RequestSpecification getRequestSpec() {
        // Fetch values dynamically inside the method
        String baseUrl = ConfigReader.getProperty("BASE_URL"); // Use exact case from your file

        if (baseUrl == null) {
            throw new RuntimeException("🛑 BASE_URL is null! Check your config.properties key name.");
        }

        return given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("x-api-key", ConfigReader.getProperty("API_KEY"));
    }

    // --- LOGIN ---
    public static Response login(LoginRequest payload) {
        // Fetch the path dynamically
        String path = ConfigReader.getProperty("LOGIN_PATH");
        return getRequestSpec()
                .body(payload)
                .when()
                .post(path);
    }

    // --- USER CREATION ---
    public static Response createUser(User payload) {
        String path = ConfigReader.getProperty("USERS_PATH");
        return getRequestSpec()
                .body(payload)
                .when()
                .post(path);
    }

    // --- USER FETCH ---
    public static Response getUser(String id) {
        String path = ConfigReader.getProperty("USERS_PATH");
        return getRequestSpec()
                .pathParam("id", id)
                .when()
                .get(path + "/{id}");
    }
}