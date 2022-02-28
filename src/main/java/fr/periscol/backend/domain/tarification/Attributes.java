package fr.periscol.backend.domain.tarification;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Attributes")
public class Attributes {

    public Attributes(Criteria criteria, String name, AttributeType type, String value, String description) {
        this.criteria = criteria;
        this.name = name;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    @Id
    private Long id;

    public Attributes() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @ManyToOne
    Criteria criteria;

    private String name;

    private AttributeType type;

    private String value;

    private String description;

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
