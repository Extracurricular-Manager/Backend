package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.service.dto.service_model.PresenceModelDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PresenceModel} and its DTO {@link PresenceModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface PresenceModelMapper extends EntityMapper<PresenceModelDTO, PresenceModel> {
}
