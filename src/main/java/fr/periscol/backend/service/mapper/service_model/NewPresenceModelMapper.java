package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.service.dto.service_model.NewPresenceModelDTO;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link PresenceModel} and its DTO {@link NewPresenceModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface NewPresenceModelMapper extends EntityMapper<NewPresenceModelDTO, PresenceModel> {

    @Mapping(target = "serviceId", expression = "java(serviceId)")
    PresenceModel toEntity(NewPresenceModelDTO s, @Context Long serviceId);

}
