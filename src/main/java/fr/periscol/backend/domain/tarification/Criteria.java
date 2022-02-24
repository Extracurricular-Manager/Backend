package fr.periscol.backend.domain.tarification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tarification.
 */
@Entity
@Table(name = "criteria")
public class Criteria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tarifications")
    private Set<Attributes> attributes = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Criteria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Criteria compute(){

        return this;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Criteria)) {
            return false;
        }
        return id != null && id.equals(((Criteria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarification{" +
            "id=" + getId() +
            "}";
    }
}
