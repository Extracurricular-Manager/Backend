package fr.periscol.backend.service.dto;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.MonthPaid;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link MonthPaid} entity.
 */
public class MonthPaidDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Long cost;

    private Boolean payed;

    private Child child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthPaidDTO)) {
            return false;
        }

        MonthPaidDTO monthPaidDTO = (MonthPaidDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monthPaidDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturationDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", cost=" + getCost() +
            ", payed='" + getPayed() + "'" +
            "}";
    }
}
