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

    private static final String ENTITY_NAME = "roleRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    public RoleResource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * {@code POST  /role-roles.json} : Create a new roleRole.
     *
     * @param roleDTO the roleRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleRoleDTO, or with status {@code 400 (Bad Request)} if the roleRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-roles")
    public ResponseEntity<RoleDTO> createRoleRole(@RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save RoleRole : {}", roleDTO);
        if (roleDTO.getName() != null) {
            throw new BadRequestAlertException("A new roleRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
            .created(new URI("/api/role-roles/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-roles.json/:id} : Updates an existing roleRole.
     *
     * @param name the id of the roleRoleDTO to save.
     * @param roleDTO the roleRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleRoleDTO,
     * or with status {@code 400 (Bad Request)} if the roleRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-roles/{name}")
    public ResponseEntity<RoleDTO> updateRoleRole(
        @PathVariable(value = "name", required = false) final String name,
        @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleRole : {}, {}", name, roleDTO);
        if (roleDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, roleDTO.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleDTO.getName().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role-roles.json/:id} : Partial updates given fields of an existing roleRole, field will ignore if it is null
     *
     * @param name the id of the roleRoleDTO to save.
     * @param roleDTO the roleRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleRoleDTO,
     * or with status {@code 400 (Bad Request)} if the roleRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role-roles/{name}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleDTO> partialUpdateRoleRole(
        @PathVariable(value = "name", required = false) final String name,
        @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleRole partially : {}, {}", name, roleDTO);
        if (roleDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, roleDTO.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleDTO> result = roleService.partialUpdate(roleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleDTO.getName().toString())
        );
    }

    /**
     * {@code GET  /role-roles.json} : get all the roleRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleRoles in body.
     */
    @GetMapping("/role-roles")
    public List<RoleDTO> getAllRoleRoles() {
        log.debug("REST request to get all RoleRoles");
        return roleService.findAll();
    }

    /**
     * {@code GET  /role-roles.json/:id} : get the "id" roleRole.
     *
     * @param name the id of the roleRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-roles/{name}")
    public ResponseEntity<RoleDTO> getRoleRole(@PathVariable String name) {
        log.debug("REST request to get RoleRole : {}", name);
        Optional<RoleDTO> roleRoleDTO = roleService.findOne(name);
        return ResponseUtil.wrapOrNotFound(roleRoleDTO);
    }

    /**
     * {@code DELETE  /role-roles.json/:id} : delete the "id" roleRole.
     *
     * @param name the id of the roleRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-roles/{name}")
    public ResponseEntity<Void> deleteRoleRole(@PathVariable String name) {
        log.debug("REST request to delete RoleRole : {}", name);
        roleService.delete(name);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name.toString()))
            .build();
    }

    @GetMapping("/role-roles/permission/{name}")
    public ResponseEntity<List<PermissionDTO>> getPermissionFromRole(@PathVariable String name){
        log.debug("REST request to get Permission from RoleRole : {}", name);
        Optional<RoleDTO> roleDTO = roleService.findOne(name);
        if(roleDTO.isEmpty()){
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        else{
            List<PermissionDTO> permissions = roleService.getPermissions(name);
            if(permissions.isEmpty()){
                throw new BadRequestAlertException("No permissions", ENTITY_NAME, "idinvalid");
            }
            return ResponseEntity.ok(permissions);
        }
    }
}
