package fr.periscol.backend.domain;

import javax.persistence.*;

@Entity
@Table(name = "MetaDataBackend")
public class MetaDataBackend {

    @Id
    @GeneratedValue()
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
