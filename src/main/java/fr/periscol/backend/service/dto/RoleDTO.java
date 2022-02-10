package fr.periscol.backend.service.dto;

import fr.periscol.backend.domain.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link Role} entity.
 */
public class RoleDTO implements Serializable {

    private String name;

    private List<PermissionDTO> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;
        if (this.name == null) {
            return false;
        }
        return Objects.equals(this.name, roleDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleDTO{" +
            ", name='" + getName() + "'" +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
