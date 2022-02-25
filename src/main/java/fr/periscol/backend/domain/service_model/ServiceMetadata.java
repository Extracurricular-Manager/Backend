package fr.periscol.backend.domain.service_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.tarification.Criteria;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ServiceMetadata.
 */
@Entity
@Table(name = "service_metadata")
public class ServiceMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "end_point")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "icon")
    private String icon;

    @OneToOne(cascade = CascadeType.ALL)
    private Permission permission;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rel_serviceMetadata__criteria", joinColumns = @JoinColumn(name = "serviceMetadata_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    @JsonIgnoreProperties(value = { "serviceMetadata" }, allowSetters = true)
    private Set<Criteria> criterias = new HashSet<>();


    public Long getId() {
        return this.id;
    }

    public ServiceMetadata endPoint(Long endPoint) {
        this.setId(endPoint);
        return this;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setId(Long endPoint) {
        this.id = endPoint;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String module) {
        this.model = module;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Criteria> getCriterias() {
        return this.criterias;
    }

    public void setCriterias(Set<Criteria> criterias) {
        this.criterias = criterias;
    }

    public ServiceMetadata criterias(Set<Criteria> criterias) {
        this.setCriterias(criterias);
        return this;
    }

    public ServiceMetadata addCriteria(Criteria criteria) {
        this.criterias.add(criteria);
        criteria.getServiceMetadata().add(this);
        return this;
    }

    public ServiceMetadata removeCriteria(Criteria criteria) {
        this.criterias.remove(criteria);
        criteria.getServiceMetadata().remove(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceMetadata)) {
            return false;
        }
        return id != null && id.equals(((ServiceMetadata) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceMetadata{" +
            "id=" + getId() +
            ", module='" + getModel() + "'" +
            ", endPoint='" + getId() + "'" +
            "}";
    }
}
