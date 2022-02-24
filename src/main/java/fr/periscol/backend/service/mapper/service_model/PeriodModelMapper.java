package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PeriodModel} and its DTO {@link PeriodModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChildMapper.class })
public interface PeriodModelMapper extends EntityMapper<PeriodModelDTO, PeriodModel> {
    @Mapping(source = "child", target = "child")
    @Mapping(source = "startBilling", target = "timeOfStartBilling")
    @Mapping(source = "begin", target = "timeOfArrival")
    @Mapping(source = "end", target = "timeOfDeparture")
    PeriodModelDTO toDto(PeriodModel s);
}
