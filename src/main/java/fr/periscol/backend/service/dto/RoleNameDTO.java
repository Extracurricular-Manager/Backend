package fr.periscol.backend.service.dto;

public class RoleNameDTO {

    private String roleName;

    public RoleNameDTO(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
