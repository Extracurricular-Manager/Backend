package fr.periscol.backend.domain.tarification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.service_model.ServiceMetadata;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * A Tarification.
 */
@Entity
@Table(name = "criteria")
public class Criteria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private TimePerspective timePerspective;

    @OneToMany(mappedBy = "criteria")
    private Set<Attributes> attributes = new HashSet<>();

    @ManyToMany(mappedBy = "criterias")
    @JsonIgnoreProperties(
            allowSetters = true
    )
    private Set<ServiceMetadata> serviceMetadata = new HashSet<>();


    public Long getId() {
        return this.id;
    }

    public Criteria id(Long id) {
        this.setId(id);
        return this;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attributes> attributes) {
        this.attributes = attributes;
    }

    public Set<ServiceMetadata> getServiceMetadata() {
        return this.serviceMetadata;
    }

    public void setServiceMetadata(Set<ServiceMetadata> serviceMetadata) {
        if (this.serviceMetadata != null) {
            this.serviceMetadata.forEach(i -> i.removeCriteria(this));
        }
        if (serviceMetadata != null) {
            serviceMetadata.forEach(i -> i.addCriteria(this));
        }
        this.serviceMetadata = serviceMetadata;
    }

    public Criteria serviceMetadata(Set<ServiceMetadata> serviceMetadata) {
        this.setServiceMetadata(serviceMetadata);
        return this;
    }

    public Criteria addServiceMetadataren(ServiceMetadata serviceMetadata) {
        this.serviceMetadata.add(serviceMetadata);
        serviceMetadata.getCriterias().add(this);
        return this;
    }

    public Criteria removeServiceMetadataren(ServiceMetadata serviceMetadata) {
        this.serviceMetadata.remove(serviceMetadata);
        serviceMetadata.getCriterias().remove(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Criteria)) {
            return false;
        }
        return id != null && id.equals(((Criteria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Criteria{" +
            "id=" + getId() +
            "}";
    }
}
