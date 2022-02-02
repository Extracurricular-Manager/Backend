package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.UserCustomRepository;
import fr.periscol.backend.service.UserCustomService;
import fr.periscol.backend.service.dto.UserCustomDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.UserCustom}.
 */
@RestController
@RequestMapping("/api")
public class UserCustomResource {

    private final Logger log = LoggerFactory.getLogger(UserCustomResource.class);

    private static final String ENTITY_NAME = "userCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCustomService userCustomService;

    private final UserCustomRepository userCustomRepository;

    public UserCustomResource(UserCustomService userCustomService, UserCustomRepository userCustomRepository) {
        this.userCustomService = userCustomService;
        this.userCustomRepository = userCustomRepository;
    }

    /**
     * {@code POST  /user-customs} : Create a new userCustom.
     *
     * @param userCustomDTO the userCustomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCustomDTO, or with status {@code 400 (Bad Request)} if the userCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-customs")
    public ResponseEntity<UserCustomDTO> createUserCustom(@RequestBody UserCustomDTO userCustomDTO) throws URISyntaxException {
        log.debug("REST request to save UserCustom : {}", userCustomDTO);
        if (userCustomDTO.getId() != null) {
            throw new BadRequestAlertException("A new userCustom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserCustomDTO result = userCustomService.save(userCustomDTO);
        return ResponseEntity
            .created(new URI("/api/user-customs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-customs/:id} : Updates an existing userCustom.
     *
     * @param id the id of the userCustomDTO to save.
     * @param userCustomDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-customs/{id}")
    public ResponseEntity<UserCustomDTO> updateUserCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserCustomDTO userCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserCustom : {}, {}", id, userCustomDTO);
        if (userCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserCustomDTO result = userCustomService.save(userCustomDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCustomDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-customs/:id} : Partial updates given fields of an existing userCustom, field will ignore if it is null
     *
     * @param id the id of the userCustomDTO to save.
     * @param userCustomDTO the userCustomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustomDTO,
     * or with status {@code 400 (Bad Request)} if the userCustomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userCustomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCustomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-customs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserCustomDTO> partialUpdateUserCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserCustomDTO userCustomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserCustom partially : {}, {}", id, userCustomDTO);
        if (userCustomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCustomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserCustomDTO> result = userCustomService.partialUpdate(userCustomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCustomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-customs} : get all the userCustoms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userCustoms in body.
     */
    @GetMapping("/user-customs")
    public List<UserCustomDTO> getAllUserCustoms(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserCustoms");
        return userCustomService.findAll();
    }

    /**
     * {@code GET  /user-customs/:id} : get the "id" userCustom.
     *
     * @param id the id of the userCustomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCustomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-customs/{id}")
    public ResponseEntity<UserCustomDTO> getUserCustom(@PathVariable Long id) {
        log.debug("REST request to get UserCustom : {}", id);
        Optional<UserCustomDTO> userCustomDTO = userCustomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userCustomDTO);
    }

    /**
     * {@code DELETE  /user-customs/:id} : delete the "id" userCustom.
     *
     * @param id the id of the userCustomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-customs/{id}")
    public ResponseEntity<Void> deleteUserCustom(@PathVariable Long id) {
        log.debug("REST request to delete UserCustom : {}", id);
        userCustomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
