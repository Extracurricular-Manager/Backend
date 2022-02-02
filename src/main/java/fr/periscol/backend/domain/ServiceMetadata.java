package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A ServiceMetadata.
 */
@Entity
@Table(name = "service_metadata")
public class ServiceMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "module")
    private String module;

    @Column(name = "end_point")
    private String endPoint;

    @JsonIgnoreProperties(value = { "serviceMetadata" }, allowSetters = true)
    @OneToOne(mappedBy = "serviceMetadata")
    private Model model;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceMetadata id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModule() {
        return this.module;
    }

    public ServiceMetadata module(String module) {
        this.setModule(module);
        return this;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public ServiceMetadata endPoint(String endPoint) {
        this.setEndPoint(endPoint);
        return this;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        if (this.model != null) {
            this.model.setServiceMetadata(null);
        }
        if (model != null) {
            model.setServiceMetadata(this);
        }
        this.model = model;
    }

    public ServiceMetadata model(Model model) {
        this.setModel(model);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
            ", module='" + getModule() + "'" +
            ", endPoint='" + getEndPoint() + "'" +
            "}";
    }
}
