package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.service.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})

public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {

    @Mapping(source = "name", target = "name")
    PermissionDTO toDto(Permission entity);

}