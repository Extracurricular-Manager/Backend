package fr.periscol.backend.service.dto.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.service.dto.ChildDTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link PeriodModel} entity.
 */
public class PeriodModelDTO implements Serializable {

    private Long id;

    private Long serviceId;

    private String name;

    private LocalDateTime timeOfArrival;

    private LocalDateTime timeOfDeparture;

    private LocalDateTime timeOfStartBilling;

    private ChildDTO child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(LocalDateTime timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }

    public LocalDateTime getTimeOfDeparture() {
        return timeOfDeparture;
    }

    public void setTimeOfDeparture(LocalDateTime timeOfDeparture) {
        this.timeOfDeparture = timeOfDeparture;
    }

    public ChildDTO getChild() {
        return child;
    }

    public void setChild(ChildDTO child) {
        this.child = child;
    }

    public LocalDateTime getTimeOfStartBilling() {
        return timeOfStartBilling;
    }

    public void setTimeOfStartBilling(LocalDateTime timeOfStartBilling) {
        this.timeOfStartBilling = timeOfStartBilling;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodModelDTO)) {
            return false;
        }

        PeriodModelDTO periodModelDTO = (PeriodModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, periodModelDTO.id);
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
