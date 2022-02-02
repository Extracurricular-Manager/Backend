package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserCustom.
 */
@Entity
@Table(name = "user_custom")
public class UserCustom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
        name = "rel_user_custom__roles",
        joinColumns = @JoinColumn(name = "user_custom_id"),
        inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Set<RoleRole> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserCustom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserCustom name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return this.login;
    }

    public UserCustom login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public UserCustom password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RoleRole> roleRoles) {
        this.roles = roleRoles;
    }

    public UserCustom roles(Set<RoleRole> roleRoles) {
        this.setRoles(roleRoles);
        return this;
    }

    public UserCustom addRoles(RoleRole roleRole) {
        this.roles.add(roleRole);
        roleRole.getUsers().add(this);
        return this;
    }

    public UserCustom removeRoles(RoleRole roleRole) {
        this.roles.remove(roleRole);
        roleRole.getUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCustom)) {
            return false;
        }
        return id != null && id.equals(((UserCustom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCustom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
