package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.ModifierRepository;
import fr.periscol.backend.service.ModifierService;
import fr.periscol.backend.service.dto.ModifierDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.Modifier}.
 */
@RestController
@RequestMapping("/api")
public class ModifierResource {

    private final Logger log = LoggerFactory.getLogger(ModifierResource.class);

    private static final String ENTITY_NAME = "modifier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModifierService modifierService;

    private final ModifierRepository modifierRepository;

    public ModifierResource(ModifierService modifierService, ModifierRepository modifierRepository) {
        this.modifierService = modifierService;
        this.modifierRepository = modifierRepository;
    }

    /**
     * {@code POST  /modifiers} : Create a new modifier.
     *
     * @param modifierDTO the modifierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modifierDTO, or with status {@code 400 (Bad Request)} if the modifier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/modifiers")
    public ResponseEntity<ModifierDTO> createModifier(@RequestBody ModifierDTO modifierDTO) throws URISyntaxException {
        log.debug("REST request to save Modifier : {}", modifierDTO);
        if (modifierDTO.getId() != null) {
            throw new BadRequestAlertException("A new modifier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModifierDTO result = modifierService.save(modifierDTO);
        return ResponseEntity
            .created(new URI("/api/modifiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /modifiers/:id} : Updates an existing modifier.
     *
     * @param id the id of the modifierDTO to save.
     * @param modifierDTO the modifierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modifierDTO,
     * or with status {@code 400 (Bad Request)} if the modifierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modifierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/modifiers/{id}")
    public ResponseEntity<ModifierDTO> updateModifier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ModifierDTO modifierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Modifier : {}, {}", id, modifierDTO);
        if (modifierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modifierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modifierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ModifierDTO result = modifierService.save(modifierDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, modifierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /modifiers/:id} : Partial updates given fields of an existing modifier, field will ignore if it is null
     *
     * @param id the id of the modifierDTO to save.
     * @param modifierDTO the modifierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modifierDTO,
     * or with status {@code 400 (Bad Request)} if the modifierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modifierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modifierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/modifiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModifierDTO> partialUpdateModifier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ModifierDTO modifierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Modifier partially : {}, {}", id, modifierDTO);
        if (modifierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modifierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modifierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModifierDTO> result = modifierService.partialUpdate(modifierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, modifierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /modifiers} : get all the modifiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modifiers in body.
     */
    @GetMapping("/modifiers")
    public List<ModifierDTO> getAllModifiers() {
        log.debug("REST request to get all Modifiers");
        return modifierService.findAll();
    }

    /**
     * {@code GET  /modifiers/:id} : get the "id" modifier.
     *
     * @param id the id of the modifierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modifierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/modifiers/{id}")
    public ResponseEntity<ModifierDTO> getModifier(@PathVariable Long id) {
        log.debug("REST request to get Modifier : {}", id);
        Optional<ModifierDTO> modifierDTO = modifierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modifierDTO);
    }

    /**
     * {@code DELETE  /modifiers/:id} : delete the "id" modifier.
     *
     * @param id the id of the modifierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/modifiers/{id}")
    public ResponseEntity<Void> deleteModifier(@PathVariable Long id) {
        log.debug("REST request to delete Modifier : {}", id);
        modifierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
