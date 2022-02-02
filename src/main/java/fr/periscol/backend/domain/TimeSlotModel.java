package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A TimeSlotModel.
 */
@Entity
@Table(name = "time_slot_model")
public class TimeSlotModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time_of_arrival")
    private LocalDate timeOfArrival;

    @Column(name = "time_of_departure")
    private LocalDate timeOfDeparture;

    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Child child;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeSlotModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TimeSlotModel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getTimeOfArrival() {
        return this.timeOfArrival;
    }

    public TimeSlotModel timeOfArrival(LocalDate timeOfArrival) {
        this.setTimeOfArrival(timeOfArrival);
        return this;
    }

    public void setTimeOfArrival(LocalDate timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public LocalDate getTimeOfDeparture() {
        return this.timeOfDeparture;
    }

    public TimeSlotModel timeOfDeparture(LocalDate timeOfDeparture) {
        this.setTimeOfDeparture(timeOfDeparture);
        return this;
    }

    public void setTimeOfDeparture(LocalDate timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public Child getChild() {
        return this.child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public TimeSlotModel child(Child child) {
        this.setChild(child);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSlotModel)) {
            return false;
        }
        return id != null && id.equals(((TimeSlotModel) o).id);
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
            ", name='" + getName() + "'" +
            ", timeOfArrival='" + getTimeOfArrival() + "'" +
            ", timeOfDeparture='" + getTimeOfDeparture() + "'" +
            "}";
    }
}
