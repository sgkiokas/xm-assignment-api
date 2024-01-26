package com.xm.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Endpoints {
    PEOPLE("people/?search=%s"),
    PEOPLE_WITH_PAGINATION("people?page=%s"),
    PEOPLE_ID("people/%s"),
    ALL_PEOPLE("people");

    private final String endpoint;
}
