package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.Family;

import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare, maximum 30€ month for any child.
 */
public class CriteriaAdelphieMaximum extends CriteriaAdelphie {

    @Override
    public float compute(Family family, Long serviceId, Date date, float price) {
        return 0;
    }

}
