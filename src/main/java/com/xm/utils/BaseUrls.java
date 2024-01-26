package com.xm.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseUrls {
    SWAPI("https://swapi.dev/api/");

    private final String baseUrl;
}
