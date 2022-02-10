package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Role;
import fr.periscol.backend.service.dto.RoleDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Named("idSet")
    Set<RoleDTO> toDtoIdSet(Set<Role> role);
}
