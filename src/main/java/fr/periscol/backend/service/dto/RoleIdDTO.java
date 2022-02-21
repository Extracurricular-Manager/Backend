package fr.periscol.backend.service.dto;

public class RoleIdDTO {

    private Long id;

    public RoleIdDTO(Long id) {
        this.id = id;
    }

    public RoleIdDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
