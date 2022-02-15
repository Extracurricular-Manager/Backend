package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.service.dto.service_model.ModifiedServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ServiceMetadata} and its DTO {@link ModifiedServiceMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModifiedServiceMapper extends EntityMapper<ModifiedServiceMetadataDTO, ServiceMetadata> { }
