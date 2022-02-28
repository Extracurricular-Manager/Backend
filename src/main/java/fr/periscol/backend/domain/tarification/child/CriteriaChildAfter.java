package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.CriteriaChild;
import fr.periscol.backend.service.service_model.PeriodModelService;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Criteria that refers to the billing of the daycare after 18h30
 * daily basis
 */
@Entity
public class CriteriaChildAfter extends CriteriaChild<PeriodModelService> {

    @Override
    public float compute(Child child, Long serviceId, Date date, float price, PeriodModelService dataService) {
        return 0;
    }
}
