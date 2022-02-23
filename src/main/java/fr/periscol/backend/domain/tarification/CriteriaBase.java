package fr.periscol.backend.domain.tarification;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tarification.
 */
public class CriteriaBase extends Criteria implements Serializable  {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tarifications")
    private Set<Attributes> attributes = new HashSet<>();

}
