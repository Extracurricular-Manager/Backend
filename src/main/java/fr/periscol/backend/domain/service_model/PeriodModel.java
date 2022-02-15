package fr.periscol.backend.domain.service_model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.Child;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A TimeSlotModel.
 */
@Entity
@Table(name = "period_model")
public class PeriodModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "begin")
    private LocalDate begin;

    @Column(name = "end")
    private LocalDate end;

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

    public PeriodModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PeriodModel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBegin() {
        return this.begin;
    }

    public PeriodModel timeOfArrival(LocalDate timeOfArrival) {
        this.setBegin(timeOfArrival);
        return this;
    }

    public void setBegin(LocalDate timeOfArrival) {
        this.begin = timeOfArrival;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public PeriodModel timeOfDeparture(LocalDate timeOfDeparture) {
        this.setEnd(timeOfDeparture);
        return this;
    }

    public void setEnd(LocalDate timeOfDeparture) {
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
            ", name='" + getName() + "'" +
            ", timeOfArrival='" + getBegin() + "'" +
            ", timeOfDeparture='" + getEnd() + "'" +
            "}";
    }
}
