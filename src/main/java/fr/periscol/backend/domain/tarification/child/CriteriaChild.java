package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

public abstract class CriteriaChild extends Criteria {

    public abstract float compute(Child child, Long serviceId, Date date, float price);

}
