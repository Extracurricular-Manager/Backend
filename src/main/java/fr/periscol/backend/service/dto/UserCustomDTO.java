package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.periscol.backend.domain.UserCustom} entity.
 */
public class UserCustomDTO implements Serializable {

    private Long id;

    private String name;

    private String login;

    private String password;

    private Set<RoleRoleDTO> roles = new HashSet<>();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleRoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCustomDTO)) {
            return false;
        }

        UserCustomDTO userCustomDTO = (UserCustomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userCustomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCustomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", roles=" + getRoles() +
            "}";
    }
}
