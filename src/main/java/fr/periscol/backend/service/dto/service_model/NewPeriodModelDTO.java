package fr.periscol.backend.service.dto.service_model;

import fr.periscol.backend.service.dto.ChildDTO;
import java.io.Serializable;
import java.time.LocalDateTime;

public class NewPeriodModelDTO implements Serializable {

    private String name;

    private LocalDateTime timeOfArrival;

    private LocalDateTime timeOfDeparture;

    private LocalDateTime timeOfStartBilling;

    private ChildDTO child;

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

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlotModelDTO{" +
                ", name='" + getName() + "'" +
                ", timeOfArrival='" + getTimeOfArrival() + "'" +
                ", timeOfDeparture='" + getTimeOfDeparture() + "'" +
                ", child=" + getChild() +
                "}";
    }
}
