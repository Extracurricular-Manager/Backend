package fr.periscol.backend.service.dto.service_model;

import fr.periscol.backend.domain.tarification.Criteria;

import java.util.List;
import java.util.Set;

public class ServiceMetadataDTO {

    private Long id;

    private String name;

    private String model;

    private String icon;

    private Set<Criteria> criterias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(Set<Criteria> criterias) {
        this.criterias = criterias;
    }
}


