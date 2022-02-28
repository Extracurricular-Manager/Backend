package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.Family;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

public abstract class CriteriaAdelphie extends Criteria {


    public abstract float compute(Family family, Long serviceId, Date date, float price);

}
