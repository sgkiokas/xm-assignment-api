package api.test.helpermodels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Validations {
    VADER("Vader"),
    YODA("Yoda");

    public final String name;
}
