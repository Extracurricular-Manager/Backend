package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A RoleRole.
 */
@Entity
@Table(name = "role_role")
public class RoleRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "permissions")
    private String permissions;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<UserCustom> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoleRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RoleRole name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public RoleRole permissions(String permissions) {
        this.setPermissions(permissions);
        return this;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Set<UserCustom> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserCustom> userCustoms) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeRoles(this));
        }
        if (userCustoms != null) {
            userCustoms.forEach(i -> i.addRoles(this));
        }
        this.users = userCustoms;
    }

    public RoleRole users(Set<UserCustom> userCustoms) {
        this.setUsers(userCustoms);
        return this;
    }

    public RoleRole addUsers(UserCustom userCustom) {
        this.users.add(userCustom);
        userCustom.getRoles().add(this);
        return this;
    }

    public RoleRole removeUsers(UserCustom userCustom) {
        this.users.remove(userCustom);
        userCustom.getRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleRole)) {
            return false;
        }
        return id != null && id.equals(((RoleRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleRole{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
