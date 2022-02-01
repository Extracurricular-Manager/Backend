package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.Role;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.periscol.backend.domain.Role}.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public RoleResource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        log.debug("REST request to get all Roles");
        return roleService.findAll();
    }

    /**
     * {@code GET  /role/:id} : get the "name" role.
     *
     * @param name the name of the role to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the role, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roles/{name}")
    public ResponseEntity<Role> getRole(@PathVariable String name) {
        log.debug("REST request to get Role : {}", name);
        Optional<Role> role = roleService.findOne(name);
        return ResponseUtil.wrapOrNotFound(role);
    }

}
