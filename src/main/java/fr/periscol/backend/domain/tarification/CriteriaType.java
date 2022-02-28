package fr.periscol.backend.domain.tarification;

import fr.periscol.backend.domain.service_model.ModelEnum;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.child.CriteriaChildAPC;
import fr.periscol.backend.domain.tarification.child.CriteriaChildAfter;
import fr.periscol.backend.domain.tarification.child.CriteriaChildBasePeriod;
import fr.periscol.backend.domain.tarification.child.CriteriaChildBasePresence;

import java.util.Arrays;
import java.util.function.Supplier;

public enum CriteriaType {

    BASE_CRITERIA_PERIOD(1, "Tarif de base garderie", CriteriaChildBasePeriod::new, ModelEnum.PERIOD),
    AFTER_CRITERIA(2, "Tarif hors base", CriteriaChildAfter::new, ModelEnum.PERIOD),
    APC_CRITERIA(3,"Tarif frere/soeur en APC", CriteriaChildAPC::new, ModelEnum.PERIOD),
    BASE_CRITERIA_PRESENCE(4, "Tarif de base cantine", CriteriaChildBasePresence::new, ModelEnum.PRESENCE)
    ;

    CriteriaType(int i, String s, Supplier<? extends Criteria> aNew, ModelEnum period) {
    }


}
