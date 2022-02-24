package fr.periscol.backend.service.dto.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.service.dto.ChildDTO;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link PresenceModel} entity.
 */
public class NewPresenceModelDTO implements Serializable {

    private String name;

    private Boolean presence;

    private LocalDate date;

    private ChildDTO child;

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


    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceModelDTO{" +
            ", name='" + getName() + "'" +
            ", presence='" + getPresence() + "'" +
            ", date='" + getDate() + "'" +
            ", child=" + getChild() +
            "}";
    }
}
