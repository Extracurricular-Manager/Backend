package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.PresenceModel;
import fr.periscol.backend.service.dto.PresenceModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PresenceModel} and its DTO {@link PresenceModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChildMapper.class })
public interface PresenceModelMapper extends EntityMapper<PresenceModelDTO, PresenceModel> {
    @Mapping(target = "child", source = "child", qualifiedByName = "id")
    PresenceModelDTO toDto(PresenceModel s);
}
