package fr.periscol.backend.service.dto.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.service.dto.ChildDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link PresenceModel} entity.
 */
public class PresenceModelDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean presence;

    private LocalDate date;

    private Long serviceId;

    private ChildDTO child;

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

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresenceModelDTO)) {
            return false;
        }

        PresenceModelDTO presenceModelDTO = (PresenceModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, presenceModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", presence='" + getPresence() + "'" +
            ", date='" + getDate() + "'" +
            ", child=" + getChild() +
            "}";
    }
}
