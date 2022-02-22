package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.Role;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.service.RoleService;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Role}.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    public RoleResource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * {@code POST  /role.json} : Create a new role.
     *
     * @param roleDTO the roleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleDTO, or with status {@code 400 (Bad Request)} if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDTO);
        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
            .created(new URI("/api/role/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role.json/:id} : Updates an existing role.
     *
     * @param id the id of the roleDTO to save.
     * @param roleDTO the roleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleDTO,
     * or with status {@code 400 (Bad Request)} if the roleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role/{id}")
    public ResponseEntity<RoleDTO> updateRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Role : {}, {}", id, roleDTO);
        if (roleDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleDTO.getName().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role/:id} : Partial updates given fields of an existing role, field will ignore if it is null
     *
     * @param id the id of the roleDTO to save.
     * @param roleDTO the roleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleDTO,
     * or with status {@code 400 (Bad Request)} if the roleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleDTO> partialUpdateRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Role partially : {}, {}", id, roleDTO);
        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleDTO> result = roleService.partialUpdate(roleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles() {
        log.debug("REST request to get all Roles");
        return roleService.findAll();
    }

    /**
     * {@code GET  /role/:id} : get the "id" role.
     *
     * @param id the id of the roleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<RoleDTO> roleDTO = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleDTO);
    }

    /**
     * {@code DELETE  /role/:id} : delete the "id" role.
     *
     * @param id the id of the roleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET /role/:id/permission} : get the permission of the "id" role.
     * @param id the id of the roleDTO to get the permissions from.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissions associated,
     * or with status {@code 400 (Bad Request)} if the Role can not be found.
     */
    @GetMapping("/role/{id}/permission")
    public ResponseEntity<List<PermissionDTO>> getPermissionFromRole(@PathVariable Long id){
        log.debug("REST request to get Permission from Role : {}", id);
        Optional<RoleDTO> roleDTO = roleService.findOne(id);
        if(roleDTO.isEmpty()){
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        else{
            List<PermissionDTO> permissions = roleService.getPermissions(id);
            return ResponseEntity.ok(permissions);
        }
    }

    /**
     * {@code PATCH /role/:id/permission} : add a permission of the "id" role.
     * @param id the id of the roleDTO to add the permission from.
     * @param permissionDTO the permissionDTO to add.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} if the permission is added,
     * or with status {@code 204 (NO_CONTENT)} if the role have already the permission.
     */
    @PatchMapping("/role/{id}/permission")
    public ResponseEntity<Void> addPermissionToRole(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
        log.debug("REST request to get add a permission to a role : {}", id);
        boolean created = roleService.addPermission(id, permissionDTO);
        if (created){
            return ResponseEntity.created(new URI("/api/permission/" + permissionDTO.getName())).build();
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * {@code DELETE  /role/:id/permission/:permission} : delete a permission of a role.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} when the permission is successfully delete.
     */
    @DeleteMapping("/role/{idRole}/permission/{idPermission}")
    public ResponseEntity<Void> deletePermissionFromRole(@PathVariable Long idRole, @PathVariable Long idPermission){
        log.debug("REST request to delete a permission {} to a role : {}", idPermission, idRole);
        roleService.deletePermission(idRole, idPermission);
        return ResponseEntity.noContent().build();
    }
}
