package fr.periscol.backend.service.dto;

import fr.periscol.backend.domain.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link User} entity.
 */
public class UserDTO implements Serializable {

    private String name;

    private String login;

    private String password;

    private boolean isActivated;

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    private Set<RoleDTO> roles = new HashSet<>();

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

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;
        if (this.name == null) {
            return false;
        }
        return Objects.equals(this.name, userDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCustomDTO{" +
            ", name='" + getName() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", roles.json=" + getRoles() +
            "}";
    }
}
