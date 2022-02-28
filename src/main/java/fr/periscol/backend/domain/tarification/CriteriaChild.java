package fr.periscol.backend.domain.tarification;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.Criteria;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @param <T> Spring service to get data
 */
@Entity
public non-sealed abstract class CriteriaChild<T> extends Criteria {

    public abstract float compute(Child child, Long serviceId, Date date, float price, T dataService);

}
