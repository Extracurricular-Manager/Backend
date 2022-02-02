package fr.periscol.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.periscol.backend.domain.Facturation} entity.
 */
public class FacturationDTO implements Serializable {

    private Long id;

    private String schoolService;

    private Long cost;

    private Boolean payed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolService() {
        return schoolService;
    }

    public void setSchoolService(String schoolService) {
        this.schoolService = schoolService;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturationDTO)) {
            return false;
        }

        FacturationDTO facturationDTO = (FacturationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturationDTO{" +
            "id=" + getId() +
            ", schoolService='" + getSchoolService() + "'" +
            ", cost=" + getCost() +
            ", payed='" + getPayed() + "'" +
            "}";
    }
}
