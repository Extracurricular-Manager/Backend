package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.UserDTO;
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
    @PostMapping("/user-customs")
    public ResponseEntity<UserDTO> createUserCustom(@RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save UserCustom : {}", userDTO);
        if (userDTO.getName() != null) {
            throw new BadRequestAlertException("A new userCustom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDTO result = userService.save(userDTO);
        return ResponseEntity
            .created(new URI("/api/user-customs/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-customs/:id} : Updates an existing userCustom.
     *
     * @param name the id of the userCustomDTO to save.
     * @param userDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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
     * @param name the id of the userCustomDTO to save.
     * @param userDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userCustomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-customs/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
    @DeleteMapping("/user-customs/{name}")
    public ResponseEntity<Void> deleteUserCustom(@PathVariable String name) {
        log.debug("REST request to delete UserCustom : {}", name);
        userService.delete(name);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name.toString()))
            .build();
    }
}
