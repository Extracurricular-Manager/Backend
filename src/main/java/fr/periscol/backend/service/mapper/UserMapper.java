package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.service.dto.NewUserDTO;
import fr.periscol.backend.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface UserMapper extends EntityMapper<UserDTO, User> {
    @Mapping(target = "activated", source = "activated")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "idSet")
    UserDTO toDto(User s);

    @Mapping(target = "activated", source = "activated")
    @Mapping(target = "removeRoles", ignore = true)
    User toEntity(UserDTO userDTO);

    @Named(value = "fromNewUserDTO")
    @Mapping(target="activated", expression = "java(false)")
    @Mapping(target="password", source="defaultPassword")
    User toUser(NewUserDTO dto);
}
