package fr.periscol.backend.service;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository repository;
    private final PermissionService permissionService;

    public RoleService(RoleRepository repository, PermissionService permissionService) {
        this.repository = repository;
        this.permissionService = permissionService;
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> findOne(String id) {
        log.debug("Request to get Permission : {}", id);
        return repository.findById(id);
    }

    public Role save(Role role) {
        final var nonExistingPermission = role.getPermissions()
            .stream()
            .filter(p -> !permissionService.exists(p))
            .toList();
        if(!nonExistingPermission.isEmpty()) {
            final var str = nonExistingPermission.stream()
                .map(Permission::getId)
                .collect(Collectors.joining(", "));
            if(nonExistingPermission.size() > 1)
                throw new IllegalArgumentException(str + " are not valid Permission");
            else
                throw new IllegalArgumentException(str + " is not valid Permission");
        }
        return repository.save(role);
    }

    public boolean exists(Role role) {
        return findOne(role.getName()).isPresent();
    }

}
