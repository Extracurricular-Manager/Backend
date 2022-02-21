package fr.periscol.backend.service;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.repository.PermissionRepository;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.mapper.PermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private final PermissionRepository repository;
    private final PermissionMapper mapper;

    public PermissionService(PermissionRepository repository, PermissionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Save a permission.
     *
     * @param permissionDto the entity to save.
     * @return the persisted entity.
     */
    public PermissionDTO save(PermissionDTO permissionDto) {
        log.debug("Request to save Permission : {}", permissionDto);
        Permission permission = mapper.toEntity(permissionDto);
        permission = repository.save(permission);
        return mapper.toDto(permission);
    }

    /**
     * Partially update a permission.
     *
     * @param permissionDto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissionDTO> partialUpdate(PermissionDTO permissionDto) {
        log.debug("Request to partially update Permission : {}", permissionDto);

        return repository
                .findById(permissionDto.getId())
                .map(existingTarifBase -> {
                    mapper.partialUpdate(existingTarifBase, permissionDto);
                    return existingTarifBase;
                })
                .map(repository::save)
                .map(mapper::toDto);
    }

    /**
     * Get all the permisions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findAll() {
        log.debug("Request to get all permissions");
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one permission by name.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    /**
     * Delete the permission by name.
     *
     * @param id the name of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        repository.deleteById(id);
    }
}
