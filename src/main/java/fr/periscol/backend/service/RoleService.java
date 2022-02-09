package fr.periscol.backend.service;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Save a roleRole.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save RoleRole : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    /**
     * Partially update a roleRole.
     *
     * @param roleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update RoleRole : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getName())
            .map(existingRoleRole -> {
                roleMapper.partialUpdate(existingRoleRole, roleDTO);

                return existingRoleRole;
            })
            .map(roleRepository::save)
            .map(roleMapper::toDto);
    }

    /**
     * Get all the roleRoles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        log.debug("Request to get all RoleRoles");
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one roleRole by id.
     *
     * @param name the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(String name) {
        log.debug("Request to get RoleRole : {}", name);
        return roleRepository.findById(name).map(roleMapper::toDto);
    }

    public List<PermissionDTO> getPermissions(String name){
        log.debug("Request to get Permissions from RoleRole : {}", name);
        return findOne(name).map(RoleDTO::getPermissions).map(ArrayList::new).orElse(new ArrayList());
    }

    /**
     * Delete the roleRole by id.
     *
     * @param name the id of the entity.
     */
    public void delete(String name) {
        log.debug("Request to delete RoleRole : {}", name);
        roleRepository.deleteById(name);
    }
}
