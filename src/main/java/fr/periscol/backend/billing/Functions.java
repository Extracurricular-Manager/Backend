package fr.periscol.backend.billing;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.service_model.PresenceModelService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Functions {

    private PeriodModelService periodService;
    private PresenceModelService presenceService;

    public float computeBasePeriod(Child child, Long serviceId, Date date, float price) {
        final var base = 0.5f;
        final var maxBound = LocalTime.of(18, 30);
        final var range = 15;

        final var entry = periodService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        final var startTime = entry.get().getTimeOfStartBilling().toLocalTime();
        var endTime = entry.get().getTimeOfDeparture().toLocalTime();

        endTime = endTime.isAfter(maxBound) ? maxBound : endTime;

        final var duration = Duration.between(startTime, endTime).get(ChronoUnit.MINUTES);
        return price + base * duration / range;
    }

    public float computeBasePresence(Child child, Long serviceId, Date date, float price) {
        final var base = 3f;

        final var entry = presenceService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        return Boolean.TRUE.equals(entry.get().getPresence()) ? price + base : price;
    }

    public float computeAfterMaxPeriod(Child child, Long serviceId, Date date, float price) {
        final var base = 5f;
        final var maxBound = LocalTime.of(18, 30);
        final var range = 15;

        final var entry = periodService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        final var endTime = entry.get().getTimeOfDeparture().toLocalTime();

        if(!endTime.isAfter(maxBound))
            return price;

        final var duration = Duration.between(endTime, maxBound).get(ChronoUnit.MINUTES);
        return price + base * duration / range;
    }

    public float computeBeforeMinPeriod(Child child, Long serviceId, Date date, float price) {
        final var base = 0f;
        final var minBound = LocalTime.of(16, 30);
        final var range = 15;

        final var entry = periodService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        final var startTime = entry.get().getTimeOfStartBilling().toLocalTime();

        if(!startTime.isBefore(minBound))
            return price;

        final var duration = Duration.between(minBound, startTime).get(ChronoUnit.MINUTES);
        return price + base * duration / range;
    }

    /** Au Mois **/

    public float computeMaximum(Child child, Date date, float price) {
        final var max = 30f;

        return Math.min(price, max);
    }

}
