package fr.periscol.backend.service.dto;

import fr.periscol.backend.domain.Permission;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.RoleRole} entity.
 */
public class RoleRoleDTO implements Serializable {

    private Long id;

    private String name;

    private List<PermissionDTO> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        if (!(o instanceof RoleRoleDTO)) {
            return false;
        }

        RoleRoleDTO roleRoleDTO = (RoleRoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleRoleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleRoleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
