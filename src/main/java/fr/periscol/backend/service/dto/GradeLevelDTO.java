package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.GradeLevel} entity.
 */
public class GradeLevelDTO implements Serializable {

    private Long id;

    private String level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeLevelDTO)) {
            return false;
        }

        GradeLevelDTO gradeLevelDTO = (GradeLevelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gradeLevelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeLevelDTO{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
