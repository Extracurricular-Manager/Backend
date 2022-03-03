package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.AttributeType;
import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.domain.tarification.CriteriaChild;
import fr.periscol.backend.service.service_model.PeriodModelService;

import javax.persistence.Entity;

import static fr.periscol.backend.domain.tarification.AttributeType.*;

import java.util.*;

/**
 * Criteria that refers to the billing of the daycare during regular schedule :
 * 7h - 8h20
 * 16h30 - 18h30
 *
 * daily basis
 */
@Entity
public class CriteriaChildBasePeriod extends CriteriaChild<PeriodModelService> {


    public CriteriaChildBasePeriod(){


        String name[] = {"price", "time","maxBound", "minBound"};
        AttributeType type[] = {CURRENCY, INT, TIME, TIME};
//        Map<Integer, Set<Object>> value = Map.of(
//                1, Set.of("price", 0.5f),
//                2, Set.of("time", 15),
//                3, Set.of("maxBound", new Time(18,30,00)),
//                4, Set.of("minbound", new Time(7,0,0))
//        );

        String value[] = {"0.5", "15","23:59","00:00"};
        String description[] = {
                "price of the prestation during regular hours",
                "time scale of the price",
                "End time of daycare",
                "Begining Time of daycare"
        };
        for(int i = 0; i < name.length; i++) {
            Attributes attribute = new Attributes(this, name[i], type[i], value[i], description[i]);
            this.getAttributes().add(attribute);
        }
    }

    @Override
    public float compute(Child child, Long serviceId, Date date, float price, PeriodModelService dataService) {
        return 0;
    }
}
