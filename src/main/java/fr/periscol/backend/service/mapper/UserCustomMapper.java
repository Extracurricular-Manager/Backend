package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.UserCustom;
import fr.periscol.backend.service.dto.UserCustomDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserCustom} and its DTO {@link UserCustomDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleRoleMapper.class })
public interface UserCustomMapper extends EntityMapper<UserCustomDTO, UserCustom> {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "idSet")
    UserCustomDTO toDto(UserCustom s);

    @Mapping(target = "removeRoles", ignore = true)
    UserCustom toEntity(UserCustomDTO userCustomDTO);
}
