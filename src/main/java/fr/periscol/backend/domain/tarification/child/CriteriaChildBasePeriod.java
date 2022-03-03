package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.AttributeType;
import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.domain.tarification.CriteriaChild;
import fr.periscol.backend.service.service_model.PeriodModelService;

import javax.persistence.Entity;

import static fr.periscol.backend.domain.tarification.AttributeType.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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


        String[] name = {"price", "time","maxBound", "minBound"};
        AttributeType[] type = {CURRENCY, INT, TIME, TIME};
//        Map<Integer, Set<Object>> value = Map.of(
//                1, Set.of("price", 0.5f),
//                2, Set.of("time", 15),
//                3, Set.of("maxBound", new Time(18,30,00)),
//                4, Set.of("minbound", new Time(7,0,0))
//        );

        String[] value = {"0.5", "15","23:59","00:00"};
        String[] description = {
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
        final var base = Float.parseFloat(attributes.get(0).getValue());
        final var maxBound = LocalTime.parse(attributes.get(2).getValue());
        final var range = Integer.parseInt(attributes.get(1).getValue());

        final var entry = dataService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        final var startTime = entry.get().getTimeOfStartBilling().toLocalTime();
        var endTime = entry.get().getTimeOfDeparture().toLocalTime();

        endTime = endTime.isAfter(maxBound) ? maxBound : endTime;

        final var duration = Duration.between(startTime, endTime).get(ChronoUnit.SECONDS);
        final long durationInMinutes = duration / 60;
        return price + base * (durationInMinutes / range);
    }
}
