package fr.periscol.backend.service;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.CriteriaAdelphie;
import fr.periscol.backend.domain.tarification.TimePerspective;
import fr.periscol.backend.domain.tarification.CriteriaChild;
import fr.periscol.backend.service.service_model.PeriodModelService;

import java.util.Date;

public class BillingCalculationService {

    private PeriodModelService periodService;

    public BillingCalculationService(PeriodModelService periodService) {
        this.periodService = periodService;
    }

    public float computeChildForDay(Child child, ServiceMetadata service, Date date) {
        return service.getCriterias().stream()
                .filter(c -> c.getTimePerspective().equals(TimePerspective.DAY))
                .reduce(0f, (i, o) -> computeMatch(o, child, service.getId(), date, i, periodService), Float::sum);
    }

    public float computeMatch(Criteria criteria, Child child, long serviceId, Date date, float price, Object service) {
        return switch(criteria) {
            case CriteriaChild criteriaChild -> criteriaChild.compute(child, serviceId, date, price, service);
            case CriteriaAdelphie criteriaAdelphie -> criteriaAdelphie.compute(child.getAdelphie(), serviceId, date, price);
            default -> throw new IllegalStateException("Unexpected value: " + criteria);
        };
    }

}
