package fr.periscol.backend.service.dto;

public class PermissionDTO {

    private String name;

    public PermissionDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
