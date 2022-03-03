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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Classroom classroom;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Family adelphie;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private GradeLevel gradeLevel;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "rel_child__diet", joinColumns = @JoinColumn(name = "child_id"), inverseJoinColumns = @JoinColumn(name = "diet_id"))
    @JsonIgnoreProperties(value = { "children" }, allowSetters = true)
    private Set<Diet> diets = new HashSet<>();

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToMany(mappedBy = "child", cascade = CascadeType.MERGE)
    private Set<PeriodModel> periodModel = new HashSet<>();

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToMany(mappedBy = "child", cascade = CascadeType.MERGE)
    private Set<PresenceModel> presenceModel = new HashSet<>();

    @JsonIgnoreProperties(value = { "child" }, allowSetters = true)
    @OneToMany(mappedBy = "child", cascade = CascadeType.MERGE)
    private Set<MonthPaid> monthPaid = new HashSet<>();

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

    public Set<PeriodModel> getPeriodModel() {
        return periodModel;
    }

    public void setPeriodModel(Set<PeriodModel> periodModel) {
        this.periodModel = periodModel;
    }

    public Set<PresenceModel> getPresenceModel() {
        return presenceModel;
    }

    public void setPresenceModel(Set<PresenceModel> presenceModel) {
        this.presenceModel = presenceModel;
    }

    public Set<MonthPaid> getMonthPaid() {
        return monthPaid;
    }

    public void setMonthPaid(Set<MonthPaid> monthPaid) {
        this.monthPaid = monthPaid;
    }

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
