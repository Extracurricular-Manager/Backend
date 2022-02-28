package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.Family;
import fr.periscol.backend.domain.tarification.Criteria;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare regarding the brother/sister total month
 * If brother/sister is at 30/month, the current child will be charged max 25€/month
 */
@Entity
public class CriteriaAdelphieMaximumBis extends CriteriaAdelphie {
    @Override
    public float compute(Family family, Long serviceId, Date date, float price) {
        return 0;
    }
}
