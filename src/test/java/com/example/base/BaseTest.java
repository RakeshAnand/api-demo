package com.example.base;

import com.example.utilities.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        // Load properties once for the entire suite
        RestAssured.baseURI = ConfigReader.getProperty("BASE_URL");

        // Global logging: All requests/responses will show in the console
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}