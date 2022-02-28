package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare during regular schedule :
 * 7h - 8h20
 * 16h30 - 18h30
 *
 * daily basis
 */
public class CriteriaChildBasePeriod extends CriteriaChild {

    @Override
    public float compute(Child child, Long serviceId, Date date, float price) {
        return 0;
    }
}
