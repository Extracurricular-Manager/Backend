package fr.periscol.backend.service;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Permission}.
 */
@Service
@Transactional
public class PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Permission> findOne(String id) {
        log.debug("Request to get Permission : {}", id);
        return repository.findById(id);
    }

    public Permission save(Permission permission) {
        return repository.save(permission);
    }

    public boolean exists(Permission permission) {
        return findOne(permission.getId()).isPresent();
    }
}
