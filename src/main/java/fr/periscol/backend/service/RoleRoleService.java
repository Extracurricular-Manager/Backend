package fr.periscol.backend.service;

import fr.periscol.backend.domain.RoleRole;
import fr.periscol.backend.repository.RoleRoleRepository;
import fr.periscol.backend.service.dto.RoleRoleDTO;
import fr.periscol.backend.service.mapper.RoleRoleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoleRole}.
 */
@Service
@Transactional
public class RoleRoleService {

    private final Logger log = LoggerFactory.getLogger(RoleRoleService.class);

    private final RoleRoleRepository roleRoleRepository;

    private final RoleRoleMapper roleRoleMapper;

    public RoleRoleService(RoleRoleRepository roleRoleRepository, RoleRoleMapper roleRoleMapper) {
        this.roleRoleRepository = roleRoleRepository;
        this.roleRoleMapper = roleRoleMapper;
    }

    /**
     * Save a roleRole.
     *
     * @param roleRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleRoleDTO save(RoleRoleDTO roleRoleDTO) {
        log.debug("Request to save RoleRole : {}", roleRoleDTO);
        RoleRole roleRole = roleRoleMapper.toEntity(roleRoleDTO);
        roleRole = roleRoleRepository.save(roleRole);
        return roleRoleMapper.toDto(roleRole);
    }

    /**
     * Partially update a roleRole.
     *
     * @param roleRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoleRoleDTO> partialUpdate(RoleRoleDTO roleRoleDTO) {
        log.debug("Request to partially update RoleRole : {}", roleRoleDTO);

        return roleRoleRepository
            .findById(roleRoleDTO.getId())
            .map(existingRoleRole -> {
                roleRoleMapper.partialUpdate(existingRoleRole, roleRoleDTO);

                return existingRoleRole;
            })
            .map(roleRoleRepository::save)
            .map(roleRoleMapper::toDto);
    }

    /**
     * Get all the roleRoles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleRoleDTO> findAll() {
        log.debug("Request to get all RoleRoles");
        return roleRoleRepository.findAll().stream().map(roleRoleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one roleRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleRoleDTO> findOne(Long id) {
        log.debug("Request to get RoleRole : {}", id);
        return roleRoleRepository.findById(id).map(roleRoleMapper::toDto);
    }

    /**
     * Delete the roleRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoleRole : {}", id);
        roleRoleRepository.deleteById(id);
    }
}
