package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMetadataMapper extends EntityMapper<ServiceMetadataDTO, ServiceMetadata> {

}
