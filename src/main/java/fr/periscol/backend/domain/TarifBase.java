package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TarifBase.
 */
@Entity
@Table(name = "tarif_base")
public class TarifBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Long price;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "tarif")
    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    private Set<Child> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TarifBase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return this.price;
    }

    public TarifBase price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public TarifBase name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return this.comment;
    }

    public TarifBase comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setTarif(null));
        }
        if (children != null) {
            children.forEach(i -> i.setTarif(this));
        }
        this.children = children;
    }

    public TarifBase children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public TarifBase addChildren(Child child) {
        this.children.add(child);
        child.setTarif(this);
        return this;
    }

    public TarifBase removeChildren(Child child) {
        this.children.remove(child);
        child.setTarif(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifBase)) {
            return false;
        }
        return id != null && id.equals(((TarifBase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifBase{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
