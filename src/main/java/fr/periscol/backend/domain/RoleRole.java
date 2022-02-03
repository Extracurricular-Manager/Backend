package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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
    @OneToMany
    private List<Permission> permissions;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<User> users = new HashSet<>();

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

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeRoles(this));
        }
        if (users != null) {
            users.forEach(i -> i.addRoles(this));
        }
        this.users = users;
    }

    public RoleRole users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public RoleRole addUsers(User user) {
        this.users.add(user);
        user.getRoles().add(this);
        return this;
    }

    public RoleRole removeUsers(User user) {
        this.users.remove(user);
        user.getRoles().remove(this);
        return this;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
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
