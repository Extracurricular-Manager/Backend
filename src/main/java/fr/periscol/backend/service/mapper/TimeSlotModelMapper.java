package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.TimeSlotModel;
import fr.periscol.backend.service.dto.TimeSlotModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeSlotModel} and its DTO {@link TimeSlotModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChildMapper.class })
public interface TimeSlotModelMapper extends EntityMapper<TimeSlotModelDTO, TimeSlotModel> {
    @Mapping(target = "child", source = "child", qualifiedByName = "id")
    TimeSlotModelDTO toDto(TimeSlotModel s);
}
