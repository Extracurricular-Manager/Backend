package fr.periscol.backend.domain.tarification;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Attributes")
public class Attributes {

    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @ManyToOne
    Criteria tarifications;

    private String name;

    private String type;

    private String value;

    private String description;
}
