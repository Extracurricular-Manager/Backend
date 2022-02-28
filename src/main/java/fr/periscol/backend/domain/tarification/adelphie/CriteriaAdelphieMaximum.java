package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.Family;
import fr.periscol.backend.domain.tarification.AttributeType;
import fr.periscol.backend.domain.tarification.Attributes;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare, maximum 30€ month for any child.
 */
@Entity
public class CriteriaAdelphieMaximum extends CriteriaAdelphie {

    public CriteriaAdelphieMaximum(){
        final var max = new Attributes();
        max.setCriteria(this);
        max.setType(AttributeType.CURRENCY);
        max.setName("Prix");
        max.setValue("30");
        max.setDescription("Prix maximal sur un mois pour ce service");
        attributes.add(max);
    }


    @Override
    public float compute(Family family, Long serviceId, Date date, float price) {
        return 0;
    }

}
