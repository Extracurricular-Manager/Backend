package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.service.dto.PermissionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {}