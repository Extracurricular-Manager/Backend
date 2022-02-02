package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.Modifier} entity.
 */
public class ModifierDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModifierDTO)) {
            return false;
        }

        ModifierDTO modifierDTO = (ModifierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, modifierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModifierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
