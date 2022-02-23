package fr.periscol.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.periscol.backend.domain.Child} entity.
 */
public class ChildDTO implements Serializable {

    private Long id;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private ClassroomDTO classroom;

    private FamilyDTO adelphie;

    private GradeLevelDTO gradeLevel;

    private Set<DietDTO> diets = new HashSet<>();

    private FacturationDTO facturation;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public ClassroomDTO getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomDTO classroom) {
        this.classroom = classroom;
    }

    public FamilyDTO getAdelphie() {
        return adelphie;
    }

    public void setAdelphie(FamilyDTO adelphie) {
        this.adelphie = adelphie;
    }

    public GradeLevelDTO getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(GradeLevelDTO gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Set<DietDTO> getDiets() {
        return diets;
    }

    public void setDiets(Set<DietDTO> diets) {
        this.diets = diets;
    }

    public FacturationDTO getFacturation() {
        return facturation;
    }

    public void setFacturation(FacturationDTO facturation) {
        this.facturation = facturation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChildDTO)) {
            return false;
        }

        ChildDTO childDTO = (ChildDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, childDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChildDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", classroom=" + getClassroom() +
            ", adelphie=" + getAdelphie() +
            ", gradeLevel=" + getGradeLevel() +
            ", diets=" + getDiets() +
            ", facturation=" + getFacturation() +
            "}";
    }
}
