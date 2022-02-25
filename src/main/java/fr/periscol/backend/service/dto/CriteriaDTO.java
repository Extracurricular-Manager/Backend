package fr.periscol.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.TimePerspective;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.sql.Time;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * A DTO for the {@link fr.periscol.backend.domain.tarification.Criteria} entity.
 */
public class CriteriaDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private TimePerspective timePerspective;

    private Set<Attributes> attributes = new HashSet<>();

    private Set<ServiceMetadata> serviceMetadata = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimePerspective getTimePerspective() {
        return timePerspective;
    }

    public void setTimePerspective(TimePerspective timePerspective) {
        this.timePerspective = timePerspective;
    }

    public Set<Attributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<Attributes> attributes) {
        this.attributes = attributes;
    }

    public Set<ServiceMetadata> getServiceMetadata() {
        return serviceMetadata;
    }

    public void setServiceMetadata(Set<ServiceMetadata> serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CriteriaDTO)) return false;
        CriteriaDTO that = (CriteriaDTO) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getTimePerspective().equals(that.getTimePerspective()) && getAttributes().equals(that.getAttributes()) && getServiceMetadata().equals(that.getServiceMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getTimePerspective(), getAttributes(), getServiceMetadata());
    }

    @Override
    public String toString() {
        return "CriteriaDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timePerspective=" + timePerspective +
                ", attributes=" + attributes +
                ", serviceMetadata=" + serviceMetadata +
                '}';
    }
}
