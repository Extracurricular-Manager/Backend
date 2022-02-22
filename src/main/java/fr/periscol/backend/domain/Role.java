package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue()
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "permissions")
    @JoinTable(name = "rel_role_permissions")
    @OneToMany(cascade=CascadeType.ALL)
    private List<Permission> permissions;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role() {
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
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

    public Role users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Role addUsers(User user) {
        this.users.add(user);
        user.getRoles().add(this);
        return this;
    }

    public Role removeUsers(User user) {
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
        if (!(o instanceof Role)) {
            return false;
        }
        return name != null && name.equals(((Role) o).name);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            ", name='" + getName() + "'" +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}
