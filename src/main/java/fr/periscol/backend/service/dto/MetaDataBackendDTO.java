package fr.periscol.backend.service.dto;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MetaDataBackendDTO implements Serializable {
    private UUID id;

    private String nameOfSchool;

    private String version;

    public MetaDataBackendDTO() {
    }

    public MetaDataBackendDTO(String nameOfSchool, String version) {
        this.nameOfSchool = nameOfSchool;
        this.version = version;
    }

    public String getNameOfSchool() {
        return nameOfSchool;
    }

    public void setNameOfSchool(String nameOfSchool) {
        this.nameOfSchool = nameOfSchool;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaDataBackendDTO that = (MetaDataBackendDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(nameOfSchool, that.nameOfSchool) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameOfSchool, version);
    }
}
