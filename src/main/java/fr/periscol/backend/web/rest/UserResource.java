package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.NewUserDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.dto.RoleIdDTO;
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
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /user} : Create a new user.
     *
     * @param userDTO the userDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDTO, or with status {@code 400 (Bad Request)} if the user has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody NewUserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        UserDTO result = userService.createNewUser(userDTO);
        return ResponseEntity
                .created(new URI("/api/user/" + result.getName()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getLogin()))
                .body(result);
    }

    /**
     * {@code PUT  /user/:id} : Updates an existing user.
     *
     * @param name    the id of the userDTO to save.
     * @param userDTO the userDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDTO,
     * or with status {@code 400 (Bad Request)} if the userDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable(value = "id", required = false) final String name,
            @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to update User : {}, {}", name, userDTO);
        checkupUser(name, userDTO);

        UserDTO result = userService.save(userDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDTO.getLogin()))
                .body(result);
    }

    /**
     * {@code PATCH  /user/:id} : Partial updates given fields of an existing user, field will ignore if it is null
     *
     * @param name    the id of the userDTO to save.
     * @param userDTO the userDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDTO,
     * or with status {@code 400 (Bad Request)} if the userDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping(value = "/user/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<UserDTO> partialUpdateUser(
            @PathVariable(value = "id", required = false) final String name,
            @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update User partially : {}, {}", name, userDTO);
        checkupUser(name, userDTO);

        Optional<UserDTO> result = userService.partialUpdate(userDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDTO.getLogin())
        );
    }

    /**
     * Some checkup concerning the User for update requests
     * @param name the login of the user to update
     * @param userDTO the new informations
     */
    public void checkupUser(String name, UserDTO userDTO) {
        if (userDTO.getLogin() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, userDTO.getLogin())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code GET  /users} : get all the users.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        log.debug("REST request to get all Users");
        return userService.findAll();
    }

    /**
     * {@code GET  /user/:id} : get the "id" user.
     *
     * @param name the id of the userDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user/{name}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String name) {
        log.debug("REST request to get User : {}", name);
        Optional<UserDTO> userDTO = userService.findOne(name);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    /**
     * {@code DELETE  /user/:id} : delete the "id" user.
     *
     * @param name the id of the userDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{name}")
    public ResponseEntity<Void> deleteUser(@PathVariable String name) {
        log.debug("REST request to delete User : {}", name);
        userService.delete(name);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name))
                .build();
    }

    /**
     * {@code GET  /user/:id/roles} : get all roles of user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles of user in body.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user/{name}/roles")
    public ResponseEntity<List<RoleDTO>> getAllRolesOfUser(@PathVariable String name) {
        log.debug("REST request to get all roles of a User : {}", name);
        return ResponseEntity.ok(userService.getAllRoles(name));
    }


    /**
     * {@code DELETE  /user/:id/role/:role} : delete a role of user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}} if role is deleted
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{name}/role/{role}")
    public ResponseEntity<Void> removeRoleFromUser(@PathVariable String name, @PathVariable Long role) {
        log.debug("REST request to remove role from a User : {}", name);
        userService.deleteRole(name, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code PATCH  /user/:name/role} : add a role to user.
     *
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} if role added, {@code 204 (NO CONTENT)} if role already associated
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/user/{name}/role")
    public ResponseEntity<Void> addRoleToUser(@PathVariable String name, @RequestBody RoleIdDTO roleName) throws URISyntaxException {
        log.debug("REST request to get add a role to a User : {}", name);
        final var result = userService.addRole(name, roleName);
        if(!result)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.created(new URI("/api/role/" + roleName)).build();
    }

    /**
     * {@code PATCH  /user/:name/reset-password} : Reset password for a User with a default password.
     * The user is disabled until the user change the password
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if password reset
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/user/{name}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable String name, @RequestBody PasswordVM password) {
        log.debug("REST request to reset password of a User : {}", name);
        userService.resetPassword(name, password);
        return ResponseEntity.ok().build();
    }

    /**
     * {@code PATCH  /user/:name/change-password} : Change the password of the user
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if password reset
     */
    @PreAuthorize("hasAuthority()")
    @PatchMapping("/user/{name}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable String name, @RequestBody PasswordVM password) {
        log.debug("REST request to change password of a User : {}", name);
        userService.changePassword(name, password);
        return ResponseEntity.ok().build();
    }

}