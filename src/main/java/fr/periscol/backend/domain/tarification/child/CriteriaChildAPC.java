package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare regarding the adelphie of a children
 * doing APC. In principle, when a brother/sioster of a child is in APC, daycare is not charged.
 *
 * daily basis
 */
public class CriteriaChildAPC extends CriteriaChild {

    @Override
    public float compute(Child child, Long serviceId, Date date, float price) {
        return 0;
    }
}
