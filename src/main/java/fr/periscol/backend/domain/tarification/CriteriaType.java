package fr.periscol.backend.domain.tarification;

import fr.periscol.backend.domain.service_model.ModelEnum;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.child.CriteriaChildAPC;
import fr.periscol.backend.domain.tarification.child.CriteriaChildAfter;
import fr.periscol.backend.domain.tarification.child.CriteriaChildBasePeriod;
import fr.periscol.backend.domain.tarification.child.CriteriaChildBasePresence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public enum CriteriaType {

    BASE_CRITERIA_PERIOD(1, "Tarif de base garderie", CriteriaChildBasePeriod::new, ModelEnum.PERIOD),
    AFTER_CRITERIA(2, "Tarif hors base", CriteriaChildAfter::new, ModelEnum.PERIOD),
    APC_CRITERIA(3,"Tarif frere/soeur en APC", CriteriaChildAPC::new, ModelEnum.PERIOD),
    BASE_CRITERIA_PRESENCE(4, "Tarif de base cantine", CriteriaChildBasePresence::new, ModelEnum.PRESENCE)
    ;

    private final int id;
    private final String name;
    private final Supplier<? extends Criteria> constructor;
    private final ModelEnum model;

    CriteriaType(int id, String name, Supplier<? extends Criteria> constructor, ModelEnum model) {
        this.id = id;
        this.name = name;
        this.constructor = constructor;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Supplier<? extends Criteria> getConstructor() {
        return constructor;
    }

    public ModelEnum getModel() {
        return model;
    }

    public static Collection<CriteriaType> getFromModelType(ModelEnum model) {
        return Arrays.stream(CriteriaType.values()).filter(m -> m.model == model).toList();
    }

    public static Optional<Criteria> getFromId(int id) {
        return Arrays.stream(CriteriaType.values())
                .filter(m -> m.id == id)
                .findFirst()
                .map(c -> c.constructor.get());
    }
}
