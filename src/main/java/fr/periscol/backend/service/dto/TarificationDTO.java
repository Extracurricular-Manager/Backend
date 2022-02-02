package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.Tarification} entity.
 */
public class TarificationDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarificationDTO)) {
            return false;
        }

        TarificationDTO tarificationDTO = (TarificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarificationDTO{" +
            "id=" + getId() +
            "}";
    }
}
