package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Facturation.
 */
@Entity
@Table(name = "monthPaid")
public class MonthPaid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "payed")
    private Boolean payed;

    @JsonIgnoreProperties(value = { "monthPaid" }, allowSetters = true)
    @OneToOne
    private Child child;


    public Long getId() {
        return this.id;
    }

    public MonthPaid id(Long id) {
        this.setId(id);
        return this;
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
        return this.cost;
    }

    public MonthPaid cost(Long cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Boolean getPayed() {
        return this.payed;
    }

    public MonthPaid payed(Boolean payed) {
        this.setPayed(payed);
        return this;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child childs) {
        this.child = childs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthPaid)) {
            return false;
        }
        return id != null && id.equals(((MonthPaid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facturation{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", cost=" + getCost() +
            ", payed='" + getPayed() + "'" +
            "}";
    }
}
