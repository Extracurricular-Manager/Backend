package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.RoleRole;
import fr.periscol.backend.service.dto.RoleRoleDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for the entity {@link RoleRole} and its DTO {@link RoleRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleRoleMapper extends EntityMapper<RoleRoleDTO, RoleRole> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<RoleRoleDTO> toDtoIdSet(Set<RoleRole> roleRole);
}
