package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;

import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare after 18h30
 * daily basis
 */
public class CriteriaChildAfter extends CriteriaChild {

    @Override
    public float compute(Child child, Long serviceId, Date date, float price) {
        return 0;
    }
}