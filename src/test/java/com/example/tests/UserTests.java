package com.example.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.example.endpoints.UserEndpoints;
import com.example.payloads.LoginRequest;
import com.example.payloads.User;
import io.restassured.response.Response;
import com.example.base.BaseTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserTests extends BaseTest {

    @DataProvider(name = "userData")
    public Object[][] getUserData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Read JSON file and convert to a List of User objects
        List<User> users = mapper.readValue(
                new File("src/test/resources/testdata/user_data.json"),
                new TypeReference<List<User>>() {
                });

        // Convert List to Object[][] for TestNG
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i);
        }
        return data;
    }

    @Test(dataProvider = "userData", enabled = true)
    public void testCreateMultipleUsers(User userPayload) {
        // Your RestAssured logic here using userPayload.getName(), etc.
        System.out.println("Testing user: " + userPayload.getName());
        System.out.println("Testing user: " + userPayload.getJob());
        System.out.println("Testing user: " + userPayload.getEmail());
        System.out.println("Testing user: " + userPayload.getPassword());
    }

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
