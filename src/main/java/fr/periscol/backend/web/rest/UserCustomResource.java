package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.NewUserDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.dto.RoleNameDTO;
import fr.periscol.backend.service.dto.UserDTO;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import fr.periscol.backend.web.rest.vm.PasswordVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class UserCustomResource {

    private final Logger log = LoggerFactory.getLogger(UserCustomResource.class);

    private static final String ENTITY_NAME = "userCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserCustomResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /user-customs} : Create a new userCustom.
     *
     * @param userDTO the userCustomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCustomDTO, or with status {@code 400 (Bad Request)} if the userCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/user-customs")
    public ResponseEntity<UserDTO> createUserCustom(@Valid @RequestBody NewUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save UserCustom : {}", userDTO);
        UserDTO result = userService.createNewUser(userDTO);
        return ResponseEntity
                .created(new URI("/api/user-customs/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /user-customs/:id} : Updates an existing userCustom.
     *
     * @param name    the id of the userCustomDTO to save.
     * @param userDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/user-customs/{id}")
    public ResponseEntity<UserDTO> updateUserCustom(
            @PathVariable(value = "id", required = false) final String name,
            @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserCustom : {}, {}", name, userDTO);
        if (userDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, userDTO.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserDTO result = userService.save(userDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDTO.getName().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /user-customs/:id} : Partial updates given fields of an existing userCustom, field will ignore if it is null
     *
     * @param name    the id of the userCustomDTO to save.
     * @param userDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userCustomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/user-customs/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<UserDTO> partialUpdateUserCustom(
            @PathVariable(value = "id", required = false) final String name,
            @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserCustom partially : {}, {}", name, userDTO);
        if (userDTO.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, userDTO.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDTO> result = userService.partialUpdate(userDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDTO.getName().toString())
        );
    }

    /**
     * {@code GET  /user-customs} : get all the userCustoms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userCustoms in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user-customs")
    public List<UserDTO> getAllUserCustoms(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserCustoms");
        return userService.findAll();
    }

    /**
     * {@code GET  /user-customs/:id} : get the "id" userCustom.
     *
     * @param name the id of the userCustomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCustomDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user-customs/{name}")
    public ResponseEntity<UserDTO> getUserCustom(@PathVariable String name) {
        log.debug("REST request to get UserCustom : {}", name);
        Optional<UserDTO> userCustomDTO = userService.findOne(name);
        return ResponseUtil.wrapOrNotFound(userCustomDTO);
    }

    /**
     * {@code DELETE  /user-customs/:id} : delete the "id" userCustom.
     *
     * @param name the id of the userCustomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user-customs/{name}")
    public ResponseEntity<Void> deleteUserCustom(@PathVariable String name) {
        log.debug("REST request to delete UserCustom : {}", name);
        userService.delete(name);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name))
                .build();
    }

    /**
     * {@code GET  /user-customs/:id/roles} : get all roles of userCustom.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles of userCustom in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user-customs/{name}/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles(@PathVariable String name) {
        log.debug("REST request to get all roles of a UserCustom : {}", name);
        return ResponseEntity.ok(userService.getAllRoles(name));
    }


    /**
     * {@code DELETE  /user-customs/:id/role/:role} : delete a role of userCustom.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles of userCustom in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user-customs/{name}/role/{role}")
    public ResponseEntity<List<RoleDTO>> deleteRole(@PathVariable String name, @PathVariable String role) {
        log.debug("REST request to get all roles of a UserCustom : {}", name);
        userService.deleteRole(name, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code PATCH  /user-customs/:name/role} : add a role to userCustom.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} if role added, {@code 204 (NO CONTENT)} if role already associated
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/user-customs/{name}/role")
    public ResponseEntity<Void> addRoleToUser(@PathVariable String name, @RequestBody RoleNameDTO roleName) throws URISyntaxException {
        log.debug("REST request to get add a role to a UserCustom : {}", name);
        final var result = userService.addRole(name, roleName);
        if(!result)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(new URI("/api/role-roles/" + roleName)).build();
    }

    /**
     * {@code PATCH  /user-customs/:name/reset-password} : Reset password for a User with a default password.
     * The user is disabled until the user change the password
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if password reset
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/user-customs/{name}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable String name, @RequestBody PasswordVM password) {
        log.debug("REST request to reset password of a UserCustom : {}", name);
        userService.resetPassword(name, password);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code PATCH  /user-customs/:name/change-password} : Change the password of the user
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if password reset
     */
    @PreAuthorize("#name == authentication.principal.username")
    @PatchMapping("/user-customs/{name}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable String name, @RequestBody PasswordVM password) {
        log.debug("REST request to change password of a UserCustom : {}", name);
        userService.changePassword(name, password);
        return ResponseEntity.ok().build();
    }

}