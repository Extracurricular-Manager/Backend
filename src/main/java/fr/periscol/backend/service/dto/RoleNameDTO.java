package fr.periscol.backend.service.dto;

public class RoleNameDTO {

    private String name;

    public RoleNameDTO(String name) {
        this.name = name;
    }

    public RoleNameDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
