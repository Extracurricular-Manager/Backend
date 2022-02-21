package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.repository.PermissionRepository;
import fr.periscol.backend.service.PermissionService;
import fr.periscol.backend.service.dto.PermissionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Permission}.
 */
@RestController
@RequestMapping("/api")
public class PermissionResource {

    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    private static final String ENTITY_NAME = "permission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionService service;

    private final PermissionRepository repository;

    public PermissionResource(PermissionService service, PermissionRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /**
     * {@code GET  /permissions} : get all the permissions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissions in body.
     */
    @GetMapping("/permissions")
    public List<PermissionDTO> getAllPermissions() {
        log.debug("REST request to get all Permissions");
        return service.findAll();
    }

    /**
     * {@code GET  /permissions/:name} : get the "id" permission.
     *
     * @param id the name of the permissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permissions/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable Long id) {
        log.debug("REST request to get Permission : {}", id);
        Optional<PermissionDTO> permissionDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionDTO);
    }
}
