package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Family.
 */
@Entity
@Table(name = "family")
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refering_parent_name")
    private String referingParentName;

    @Column(name = "refering_parent_surname")
    private String referingParentSurname;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column(name = "postal_adress")
    private String postalAdress;

    @OneToMany(mappedBy = "adelphie")
    @JsonIgnoreProperties(
        value = { "classroom", "adelphie", "gradeLevel", "diets", "timeSlotModel", "presenceModel", "tarif", "facturation" },
        allowSetters = true
    )
    private Set<Child> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Family id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferingParentName() {
        return this.referingParentName;
    }

    public Family referingParentName(String referingParentName) {
        this.setReferingParentName(referingParentName);
        return this;
    }

    public void setReferingParentName(String referingParentName) {
        this.referingParentName = referingParentName;
    }

    public String getReferingParentSurname() {
        return this.referingParentSurname;
    }

    public Family referingParentSurname(String referingParentSurname) {
        this.setReferingParentSurname(referingParentSurname);
        return this;
    }

    public void setReferingParentSurname(String referingParentSurname) {
        this.referingParentSurname = referingParentSurname;
    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public Family telephoneNumber(String telephoneNumber) {
        this.setTelephoneNumber(telephoneNumber);
        return this;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getPostalAdress() {
        return this.postalAdress;
    }

    public Family postalAdress(String postalAdress) {
        this.setPostalAdress(postalAdress);
        return this;
    }

    public void setPostalAdress(String postalAdress) {
        this.postalAdress = postalAdress;
    }

    public Set<Child> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Child> children) {
        if (this.children != null) {
            this.children.forEach(i -> i.setAdelphie(null));
        }
        if (children != null) {
            children.forEach(i -> i.setAdelphie(this));
        }
        this.children = children;
    }

    public Family children(Set<Child> children) {
        this.setChildren(children);
        return this;
    }

    public Family addChildren(Child child) {
        this.children.add(child);
        child.setAdelphie(this);
        return this;
    }

    public Family removeChildren(Child child) {
        this.children.remove(child);
        child.setAdelphie(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Family)) {
            return false;
        }
        return id != null && id.equals(((Family) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Family{" +
            "id=" + getId() +
            ", referingParentName='" + getReferingParentName() + "'" +
            ", referingParentSurname='" + getReferingParentSurname() + "'" +
            ", telephoneNumber='" + getTelephoneNumber() + "'" +
            ", postalAdress='" + getPostalAdress() + "'" +
            "}";
    }
}
