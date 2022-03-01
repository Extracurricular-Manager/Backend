package fr.periscol.backend.domain.service_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.Child;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A TimeSlotModel.
 */
@Entity
@Table(name = "period_model")
public class PeriodModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "begin")
    private LocalDateTime begin;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "start_billing")
    private LocalDateTime startBilling;

    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    @ManyToOne
    private Child child;

    public Long getId() {
        return this.id;
    }

    public PeriodModel id(Long id) {
        this.setId(id);
        return this;
    }

    public LocalDateTime getStartBilling() {
        return startBilling;
    }

    public void setStartBilling(LocalDateTime startBilling) {
        this.startBilling = startBilling;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBegin() {
        return this.begin;
    }

    public PeriodModel timeOfArrival(LocalDateTime timeOfArrival) {
        this.setBegin(timeOfArrival);
        return this;
    }

    public void setBegin(LocalDateTime timeOfArrival) {
        this.begin = timeOfArrival;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public PeriodModel timeOfDeparture(LocalDateTime timeOfDeparture) {
        this.setEnd(timeOfDeparture);
        return this;
    }

    public void setEnd(LocalDateTime timeOfDeparture) {
        this.end = timeOfDeparture;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public PeriodModel child(Child child) {
        this.setChild(child);
        return this;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodModel)) {
            return false;
        }
        return id != null && id.equals(((PeriodModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlotModel{" +
            "id=" + getId() +
            ", timeOfArrival='" + getBegin() + "'" +
            ", timeOfDeparture='" + getEnd() + "'" +
            "}";
    }
}
