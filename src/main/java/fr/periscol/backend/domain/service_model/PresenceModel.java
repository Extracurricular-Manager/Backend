package fr.periscol.backend.domain.service_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.Child;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PresenceModel.
 */
@Entity
@Table(name = "presence_model")
public class PresenceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "presence")
    private Boolean presence;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "service_id")
    private Long serviceId;

    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    @ManyToOne
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PresenceModel id(Long id) {
        this.setId(id);
        return this;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPresence() {
        return this.presence;
    }

    public PresenceModel presence(Boolean presence) {
        this.setPresence(presence);
        return this;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PresenceModel date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public PresenceModel child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresenceModel)) {
            return false;
        }
        return id != null && id.equals(((PresenceModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceModel{" +
            "id=" + getId() +
            ", presence='" + getPresence() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
