package com.xm.api;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestClient {
    public Response get(String url, String path, Object... pathParams) {
        RequestSpecification requestSpecification = given();
        addPathParams(requestSpecification, pathParams);

        return requestSpecification.when()
                .get(url.concat(path))
                .thenReturn();
    }

    public Response get(String url, Object... pathParams) {
        RequestSpecification requestSpecification = given();
        addPathParams(requestSpecification, pathParams);

        return requestSpecification.when()
                .get(url)
                .thenReturn();
    }

    private void addPathParams(RequestSpecification requestSpecification, Object... pathParams) {
        IntStream.range(0, pathParams.length / 2)
                .mapToObj(i -> new Object[]{pathParams[i * 2].toString(), pathParams[i * 2 + 1]})
                .forEach(params -> requestSpecification.pathParam(params[0].toString(), params[1]));
    }
}
