package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

/**
 * Criteria that refers to the billing of the canteen on regular basis (no special diet nor
 * special condition)
 *
 * daily basis
 *
 */
public class CriteriaChildBasePresence extends CriteriaChild {

    @Override
    public float compute(Child child, Long serviceId, Date date, float price) {
        return 0;
    }
}
