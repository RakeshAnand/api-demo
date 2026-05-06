package com.example.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.example.endpoints.UserEndpoints;
import com.example.payloads.LoginRequest;
import com.example.payloads.User;
import io.restassured.response.Response;

public class UserTests {

    private User userPayload;

    @BeforeClass
    public void setupData() {
        // Payload for user creation (name + job)
        userPayload = User.builder()
                .name("Tenali Rama")
                .job("Poet")
                .build();
    }

    @Test(priority = 1, enabled = true)
    public void testLoginSuccess() {
        // Correct login payload (email + password only)
        LoginRequest credentials = new LoginRequest("eve.holt@reqres.in", "cityslicka");

        Response response = UserEndpoints.login(credentials);

        response.then().statusCode(200);
        String token = response.jsonPath().getString("token");

        Assert.assertNotNull(token, "Token should not be null on successful login");
        System.out.println("Login Token: " + token);
    }

    @Test(priority = 2, enabled = true)
    public void testLoginFailure() {
        // Missing password case
        LoginRequest credentials = new LoginRequest("peter@klaven", null);

        Response response = UserEndpoints.login(credentials);

        response.then().statusCode(400);
        String error = response.jsonPath().getString("error");

        Assert.assertEquals(error, "Missing password");
    }

    @Test(priority = 3, enabled = true)
    public void testPostUser() {
        Response response = UserEndpoints.createUser(userPayload);

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.jsonPath().getString("name"), userPayload.getName());
        Assert.assertEquals(response.jsonPath().getString("job"), userPayload.getJob());
    }

    @Test(priority = 4, enabled = true)
    public void testGetUser() {
        Response response = UserEndpoints.getUser("2");

        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("data.first_name"), "Janet");
    }
}
