package fr.periscol.backend.domain.tarification.adelphie;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.Family;
import fr.periscol.backend.domain.tarification.Criteria;

import java.util.Date;

public class CriteriaAdelphieMaximumBis extends CriteriaAdelphie {
    @Override
    public float compute(Family family, Long serviceId, Date date, float price) {
        return 0;
    }
}
