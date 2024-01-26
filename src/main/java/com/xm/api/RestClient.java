package com.xm.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestClient {
    public Response get(String url, String path) {
        RequestSpecification requestSpecification = given();

        return requestSpecification.when()
                .get(url.concat(path))
                .thenReturn();
    }

    public Response get(String url) {
        RequestSpecification requestSpecification = given();

        return requestSpecification.when()
                .get(url)
                .thenReturn();
    }
}
