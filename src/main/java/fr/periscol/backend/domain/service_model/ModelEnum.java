package fr.periscol.backend.domain.service_model;

import java.util.Arrays;

public enum ModelEnum {

    PERIOD("period"),
    PRESENCE("presence");

    private final String id;

    ModelEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static boolean exists(String name) {
        return Arrays.stream(ModelEnum.values()).anyMatch(m -> m.id.equals(name));
    }
}
