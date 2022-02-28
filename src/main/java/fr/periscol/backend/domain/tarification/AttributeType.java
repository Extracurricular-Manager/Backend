package fr.periscol.backend.domain.tarification;

import java.util.Arrays;
import java.util.Optional;

public enum AttributeType {

    CURRENCY("currency"),
    FLOAT("float"),
    INT("int"),
    TIME("time");

    final String str;

    AttributeType(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public static Optional<AttributeType> getAttributeType(String str) {
        return Arrays.stream(AttributeType.values()).filter(a -> a.str.equals(str)).findAny();
    }
}
