package fr.periscol.backend.service;

import fr.periscol.backend.domain.Role;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.mapper.RoleMapper;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionService = permissionService;
    }

    /**
     * Save a role.
     *
     * @param roleDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    /**
     * Partially update a role.
     *
     * @param roleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getId())
            .map(existingRole -> {
                roleMapper.partialUpdate(existingRole, roleDTO);

                return existingRole;
            })
            .map(roleRepository::save)
            .map(roleMapper::toDto);
    }

    /**
     * Get all the roles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        log.debug("Request to get all Roles");
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one role by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findById(id).map(roleMapper::toDto);
    }

    /**
     * Get the list of the permissions of a given role
     * @param id the id of the role
     * @return the list of the permissions
     */
    public List<PermissionDTO> getPermissions(Long id) {
        log.debug("Request to get Permissions from Role : {}", id);
        return findOne(id).map(RoleDTO::getPermissions).map(ArrayList::new).orElse(new ArrayList<>());
    }

    /**
     * Add a permission to a given role.
     *
     * @param id the id of the role to add the permission
     * @param permissionDTO the name of the role
     * @return true if the permission is added, false if the permission is already present for this role
     */
    public boolean addPermission(Long id, PermissionDTO permissionDTO) {
        log.debug("Request to add a permission to a role : {}", id);
        final var permissionOpt = permissionService.findOne(permissionDTO.getId());
        final var roleOpt = findOne(id);
        if(permissionOpt.isPresent() && roleOpt.isPresent()) {
            final var permission = permissionOpt.get();
            final var roleDTO = roleOpt.get();
            if(! roleDTO.getPermissions().contains(permission)) {
                final List<PermissionDTO> permissions = new ArrayList<>(roleDTO.getPermissions());
                permissions.add(permission);
                final var newRole = new RoleDTO();
                newRole.setName(roleDTO.getName());
                newRole.setPermissions(permissions);
                partialUpdate(newRole);
                return true;
            } else
                return false;
        } else if(permissionOpt.isEmpty())
            throw new NotFoundAlertException("Specified permission does not exist.");
        else
            throw new NotFoundAlertException("Specified role does not exist.");
    }

    /**
     * Delete a Permission of a given role
     *
     * @param idRole the name of the role to delete from.
     * @param idPermission the id of the permission to delete.
     */
    public void deletePermission(Long idRole, Long idPermission){
        log.debug("Request to delete a permission {} to a role : {}", idPermission, idRole);
        Optional<RoleDTO> roleDTOOptional = findOne(idRole);
        if(roleDTOOptional.isPresent()){
            RoleDTO updatedRoleDTO = roleDTOOptional.get();
            Optional<PermissionDTO> permissionDTOOptional = permissionService.findOne(idPermission);
            if(permissionDTOOptional.isEmpty()){
                throw new NotFoundAlertException("Specified permission does not exist.");
            }
            PermissionDTO permissionDTO = permissionDTOOptional.get();
            if(! updatedRoleDTO.getPermissions().contains(permissionDTO)){
                throw new NotFoundAlertException("Specified role does not have this permission.");
            }
            List<PermissionDTO> permissionDTOList = updatedRoleDTO.getPermissions();
            permissionDTOList.remove(permissionDTO);
            updatedRoleDTO.setPermissions(permissionDTOList);
            partialUpdate(updatedRoleDTO);
        }
        else{
            throw new NotFoundAlertException("Specified role does not exist.");
        }
    }

    /**
     * Delete the role by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
    }
}
