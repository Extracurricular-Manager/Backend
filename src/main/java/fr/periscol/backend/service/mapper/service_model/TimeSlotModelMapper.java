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
public interface TimeSlotModelMapper extends EntityMapper<PeriodModelDTO, PeriodModel> {
    @Mapping(target = "child", source = "child", qualifiedByName = "id")
    PeriodModelDTO toDto(PeriodModel s);
}
