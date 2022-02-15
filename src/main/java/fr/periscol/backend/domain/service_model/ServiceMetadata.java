package fr.periscol.backend.domain.service_model;

import javax.persistence.*;
import java.io.Serializable;

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

    public Long getId() {
        return this.id;
    }

    public ServiceMetadata endPoint(Long endPoint) {
        this.setId(endPoint);
        return this;
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
