package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.RoleRoleRepository;
import fr.periscol.backend.service.RoleRoleService;
import fr.periscol.backend.service.dto.RoleRoleDTO;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.periscol.backend.domain.RoleRole}.
 */
@RestController
@RequestMapping("/api")
public class RoleRoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleRoleResource.class);

    private static final String ENTITY_NAME = "roleRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleRoleService roleRoleService;

    private final RoleRoleRepository roleRoleRepository;

    public RoleRoleResource(RoleRoleService roleRoleService, RoleRoleRepository roleRoleRepository) {
        this.roleRoleService = roleRoleService;
        this.roleRoleRepository = roleRoleRepository;
    }

    /**
     * {@code POST  /role-roles} : Create a new roleRole.
     *
     * @param roleRoleDTO the roleRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleRoleDTO, or with status {@code 400 (Bad Request)} if the roleRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-roles")
    public ResponseEntity<RoleRoleDTO> createRoleRole(@RequestBody RoleRoleDTO roleRoleDTO) throws URISyntaxException {
        log.debug("REST request to save RoleRole : {}", roleRoleDTO);
        if (roleRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleRoleDTO result = roleRoleService.save(roleRoleDTO);
        return ResponseEntity
            .created(new URI("/api/role-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-roles/:id} : Updates an existing roleRole.
     *
     * @param id the id of the roleRoleDTO to save.
     * @param roleRoleDTO the roleRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleRoleDTO,
     * or with status {@code 400 (Bad Request)} if the roleRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-roles/{id}")
    public ResponseEntity<RoleRoleDTO> updateRoleRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleRoleDTO roleRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleRole : {}, {}", id, roleRoleDTO);
        if (roleRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleRoleDTO result = roleRoleService.save(roleRoleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleRoleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role-roles/:id} : Partial updates given fields of an existing roleRole, field will ignore if it is null
     *
     * @param id the id of the roleRoleDTO to save.
     * @param roleRoleDTO the roleRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleRoleDTO,
     * or with status {@code 400 (Bad Request)} if the roleRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role-roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleRoleDTO> partialUpdateRoleRole(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleRoleDTO roleRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleRole partially : {}, {}", id, roleRoleDTO);
        if (roleRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleRoleDTO> result = roleRoleService.partialUpdate(roleRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /role-roles} : get all the roleRoles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleRoles in body.
     */
    @GetMapping("/role-roles")
    public List<RoleRoleDTO> getAllRoleRoles() {
        log.debug("REST request to get all RoleRoles");
        return roleRoleService.findAll();
    }

    /**
     * {@code GET  /role-roles/:id} : get the "id" roleRole.
     *
     * @param id the id of the roleRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-roles/{id}")
    public ResponseEntity<RoleRoleDTO> getRoleRole(@PathVariable Long id) {
        log.debug("REST request to get RoleRole : {}", id);
        Optional<RoleRoleDTO> roleRoleDTO = roleRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleRoleDTO);
    }

    /**
     * {@code DELETE  /role-roles/:id} : delete the "id" roleRole.
     *
     * @param id the id of the roleRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-roles/{id}")
    public ResponseEntity<Void> deleteRoleRole(@PathVariable Long id) {
        log.debug("REST request to delete RoleRole : {}", id);
        roleRoleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
