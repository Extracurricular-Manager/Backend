package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.PresenceModelRepository;
import fr.periscol.backend.service.PresenceModelService;
import fr.periscol.backend.service.dto.PresenceModelDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.PresenceModel}.
 */
@RestController
@RequestMapping("/api")
public class PresenceModelResource {

    private final Logger log = LoggerFactory.getLogger(PresenceModelResource.class);

    private static final String ENTITY_NAME = "presenceModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresenceModelService presenceModelService;

    private final PresenceModelRepository presenceModelRepository;

    public PresenceModelResource(PresenceModelService presenceModelService, PresenceModelRepository presenceModelRepository) {
        this.presenceModelService = presenceModelService;
        this.presenceModelRepository = presenceModelRepository;
    }

    /**
     * {@code POST  /presence-models} : Create a new presenceModel.
     *
     * @param presenceModelDTO the presenceModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presenceModelDTO, or with status {@code 400 (Bad Request)} if the presenceModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presence-models")
    public ResponseEntity<PresenceModelDTO> createPresenceModel(@RequestBody PresenceModelDTO presenceModelDTO) throws URISyntaxException {
        log.debug("REST request to save PresenceModel : {}", presenceModelDTO);
        if (presenceModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new presenceModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PresenceModelDTO result = presenceModelService.save(presenceModelDTO);
        return ResponseEntity
            .created(new URI("/api/presence-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presence-models/:id} : Updates an existing presenceModel.
     *
     * @param id the id of the presenceModelDTO to save.
     * @param presenceModelDTO the presenceModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presenceModelDTO,
     * or with status {@code 400 (Bad Request)} if the presenceModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presenceModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presence-models/{id}")
    public ResponseEntity<PresenceModelDTO> updatePresenceModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresenceModelDTO presenceModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PresenceModel : {}, {}", id, presenceModelDTO);
        if (presenceModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presenceModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presenceModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PresenceModelDTO result = presenceModelService.save(presenceModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, presenceModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /presence-models/:id} : Partial updates given fields of an existing presenceModel, field will ignore if it is null
     *
     * @param id the id of the presenceModelDTO to save.
     * @param presenceModelDTO the presenceModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presenceModelDTO,
     * or with status {@code 400 (Bad Request)} if the presenceModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the presenceModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the presenceModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/presence-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PresenceModelDTO> partialUpdatePresenceModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresenceModelDTO presenceModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PresenceModel partially : {}, {}", id, presenceModelDTO);
        if (presenceModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presenceModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presenceModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PresenceModelDTO> result = presenceModelService.partialUpdate(presenceModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, presenceModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /presence-models} : get all the presenceModels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presenceModels in body.
     */
    @GetMapping("/presence-models")
    public List<PresenceModelDTO> getAllPresenceModels() {
        log.debug("REST request to get all PresenceModels");
        return presenceModelService.findAll();
    }

    /**
     * {@code GET  /presence-models/:id} : get the "id" presenceModel.
     *
     * @param id the id of the presenceModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presenceModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presence-models/{id}")
    public ResponseEntity<PresenceModelDTO> getPresenceModel(@PathVariable Long id) {
        log.debug("REST request to get PresenceModel : {}", id);
        Optional<PresenceModelDTO> presenceModelDTO = presenceModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presenceModelDTO);
    }

    /**
     * {@code DELETE  /presence-models/:id} : delete the "id" presenceModel.
     *
     * @param id the id of the presenceModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presence-models/{id}")
    public ResponseEntity<Void> deletePresenceModel(@PathVariable Long id) {
        log.debug("REST request to delete PresenceModel : {}", id);
        presenceModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
