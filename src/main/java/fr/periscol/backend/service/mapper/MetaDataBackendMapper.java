package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.service.dto.MetaDataBackendDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface MetaDataBackendMapper extends EntityMapper<MetaDataBackendDTO, MetaDataBackend>{

    @Mapping(target = "nameOfSchool", source = "nameOfSchool")
    MetaDataBackendDTO toDto(MetaDataBackend entity);
}
