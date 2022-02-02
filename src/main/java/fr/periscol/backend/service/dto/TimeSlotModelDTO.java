package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.TimeSlotModel} entity.
 */
public class TimeSlotModelDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate timeOfArrival;

    private LocalDate timeOfDeparture;

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

    public LocalDate getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(LocalDate timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public LocalDate getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(LocalDate timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSlotModelDTO)) {
            return false;
        }

        TimeSlotModelDTO timeSlotModelDTO = (TimeSlotModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeSlotModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlotModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", timeOfArrival='" + getTimeOfArrival() + "'" +
            ", timeOfDeparture='" + getTimeOfDeparture() + "'" +
            ", child=" + getChild() +
            "}";
    }
}
