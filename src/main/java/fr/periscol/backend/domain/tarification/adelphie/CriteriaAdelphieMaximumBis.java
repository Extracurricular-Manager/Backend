package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Family;
import fr.periscol.backend.domain.tarification.AttributeType;
import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.domain.tarification.CriteriaAdelphie;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare regarding the brother/sister total month
 * If brother/sister is at 30/month, the current child will be charged max 25€/month
 */
@Entity
public class CriteriaAdelphieMaximumBis extends CriteriaAdelphie {

    public CriteriaAdelphieMaximumBis(){
        final var max = new Attributes();
        max.setCriteria(this);
        max.setType(AttributeType.CURRENCY);
        max.setName("Prix");
        max.setValue("25");
        max.setDescription("Prix maximal sur un mois pour ce service si un frere ou une soeur" +
                "est déjà au maximum de 30€ par mois");
        attributes.add(max);
    }


    @Override
    public float compute(Family family, Long serviceId, Date date, float price) {
        return 0;
    }
}
