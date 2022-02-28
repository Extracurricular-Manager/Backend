package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.tarification.Criteria;

public abstract class CriteriaChild extends Criteria {

    public abstract float compute();

}
