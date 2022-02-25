package fr.periscol.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.domain.service_model.PresenceModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private LocalDate birthday;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Classroom classroom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Family adelphie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private GradeLevel gradeLevel;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rel_child__diet", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "diet_id"))
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Set<Diet> diets = new HashSet<>();

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToOne(mappedBy = "child")
    private PeriodModel periodModel;

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToOne(mappedBy = "child")
    private PresenceModel presenceModel;


    @OneToOne(mappedBy = "child")
    private MonthPaid monthPaid;

    public Long getId() {
        return this.id;
    }

    public Child id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Child name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Child surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public Child birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Classroom getClassroom() {
        return this.classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Child classroom(Classroom classroom) {
        this.setClassroom(classroom);
        return this;
    }

    public Family getAdelphie() {
        return this.adelphie;
    }

    public void setAdelphie(Family family) {
        this.adelphie = family;
    }

    public Child adelphie(Family family) {
        this.setAdelphie(family);
        return this;
    }

    public GradeLevel getGradeLevel() {
        return this.gradeLevel;
    }

    public void setGradeLevel(GradeLevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Child gradeLevel(GradeLevel gradeLevel) {
        this.setGradeLevel(gradeLevel);
        return this;
    }

    public Set<Diet> getDiets() {
        return this.diets;
    }

    public void setDiets(Set<Diet> diets) {
        this.diets = diets;
    }

    public Child diets(Set<Diet> diets) {
        this.setDiets(diets);
        return this;
    }

    public Child addDiet(Diet diet) {
        this.diets.add(diet);
        diet.getChildren().add(this);
        return this;
    }

    public Child removeDiet(Diet diet) {
        this.diets.remove(diet);
        diet.getChildren().remove(this);
        return this;
    }

    public PeriodModel getTimeSlotModel() {
        return this.periodModel;
    }

    public void setTimeSlotModel(PeriodModel periodModel) {
        if (this.periodModel != null) {
            this.periodModel.setChild(null);
        }
        if (periodModel != null) {
            periodModel.setChild(this);
        }
        this.periodModel = periodModel;
    }

    public Child timeSlotModel(PeriodModel periodModel) {
        this.setTimeSlotModel(periodModel);
        return this;
    }

    public PresenceModel getPresenceModel() {
        return this.presenceModel;
    }

    public void setPresenceModel(PresenceModel presenceModel) {
        if (this.presenceModel != null) {
            this.presenceModel.setChild(null);
        }
        if (presenceModel != null) {
            presenceModel.setChild(this);
        }
        this.presenceModel = presenceModel;
    }

    public Child presenceModel(PresenceModel presenceModel) {
        this.setPresenceModel(presenceModel);
        return this;
    }

    public MonthPaid getMonthPaid() {
        return monthPaid;
    }

    public void setMonthPaid(MonthPaid monthPaid) {
        this.monthPaid = monthPaid;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Child)) {
            return false;
        }
        return id != null && id.equals(((Child) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Child{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", surname='" + getSurname() + "'" +
                ", birthday='" + getBirthday() + "'" +
                "}";
    }
}
