package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Facturation.
 */
@Entity
@Table(name = "facturation")
public class Facturation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "school_service")
    private String schoolService;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "payed")
    private Boolean payed;

    @OneToMany(mappedBy = "facturation")
    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    private Set<Child> childs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facturation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolService() {
        return this.schoolService;
    }

    public Facturation schoolService(String schoolService) {
        this.setSchoolService(schoolService);
        return this;
    }

    public void setSchoolService(String schoolService) {
        this.schoolService = schoolService;
    }

    public Long getCost() {
        return this.cost;
    }

    public Facturation cost(Long cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Boolean getPayed() {
        return this.payed;
    }

    public Facturation payed(Boolean payed) {
        this.setPayed(payed);
        return this;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Set<Child> getChilds() {
        return this.childs;
    }

    public void setChilds(Set<Child> children) {
        if (this.childs != null) {
            this.childs.forEach(i -> i.setFacturation(null));
        }
        if (children != null) {
            children.forEach(i -> i.setFacturation(this));
        }
        this.childs = children;
    }

    public Facturation childs(Set<Child> children) {
        this.setChilds(children);
        return this;
    }

    public Facturation addChilds(Child child) {
        this.childs.add(child);
        child.setFacturation(this);
        return this;
    }

    public Facturation removeChilds(Child child) {
        this.childs.remove(child);
        child.setFacturation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facturation)) {
            return false;
        }
        return id != null && id.equals(((Facturation) o).id);
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
            ", schoolService='" + getSchoolService() + "'" +
            ", cost=" + getCost() +
            ", payed='" + getPayed() + "'" +
            "}";
    }
}
