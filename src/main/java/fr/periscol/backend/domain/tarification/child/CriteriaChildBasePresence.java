package fr.periscol.backend.domain.tarification.child;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.tarification.AttributeType;
import fr.periscol.backend.domain.tarification.Attributes;
import fr.periscol.backend.service.service_model.PresenceModelService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Criteria that refers to the billing of the canteen on regular basis (no special diet nor
 * special condition)
 *
 * daily basis
 *
 */
@Entity
public class CriteriaChildBasePresence extends CriteriaChild {

    public CriteriaChildBasePresence(PresenceModelService presenceModelService) {
        final var currency = new Attributes();
        currency.setCriteria(this);
        currency.setType(AttributeType.CURRENCY);
        currency.setName("Prix");
        currency.setValue("0");
        currency.setDescription("Prix de base pour ce service");
        attributes.add(currency);

    }

    public CriteriaChildBasePresence() {

    }

    @Override
    public float compute(Child child, Long serviceId, Date date, float price) {
        final var entry = presenceModelService.findOneForDay(child.getId(), serviceId, date);
        if(entry.isEmpty())
            return price;

        return Boolean.TRUE.equals(entry.get().getPresence()) ? price + base : price;
    }
}
