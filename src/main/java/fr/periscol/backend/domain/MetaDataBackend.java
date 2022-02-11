package fr.periscol.backend.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MetaDataBackend")
public class MetaDataBackend {

    @Id
    @Column(name = "name_of_school", nullable = false)
    private String nameOfSchool;

    private String version;

    public MetaDataBackend(){}

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
}
