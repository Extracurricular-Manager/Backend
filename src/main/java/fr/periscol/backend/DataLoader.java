package fr.periscol.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.domain.User;
import fr.periscol.backend.service.RoleService;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.NewUserDTO;
import fr.periscol.backend.service.dto.RoleNameDTO;
import fr.periscol.backend.service.mapper.RoleMapper;
import fr.periscol.backend.service.mapper.UserMapper;
import fr.periscol.backend.web.rest.vm.PasswordVM;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader {

    private final RoleService roleService;
    private final UserService userService;

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public DataLoader(RoleService roleService, UserService userService, RoleMapper roleMapper, UserMapper userMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void load() throws IOException {
        final var mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        final var roles = mapper.readValue(readResource("data/roles.json"), new TypeReference<List<Role>>(){});
        roles.stream().filter(r -> roleService.findOne(r.getName()).isEmpty()).forEach(r -> roleService.save(roleMapper.toDto(r)));

        final var users = mapper.readValue(readResource("data/users.json"), new TypeReference<List<User>>() {});

        users.stream().filter(u -> userService.findOne(u.getName()).isEmpty())
                .map(this::toNewUserDto)
                .forEach(userService::createNewUser);

        // Active user created from resources/data
        for(final var user: users)
            for(final var role: user.getRoles()) {
                userService.addRole(user.getName(), new RoleNameDTO(role.getName()));
                userService.changePassword(user.getName(), new PasswordVM(user.getPassword()));
            }
    }

    private NewUserDTO toNewUserDto(User user) {
        final var dto = new NewUserDTO();
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setDefaultPassword(user.getPassword());
        return dto;
    }

    private InputStream readResource(String name) {
        return DataLoader.class.getClassLoader().getResourceAsStream(name);
    }
}
