package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.TarifBaseRepository;
import fr.periscol.backend.service.TarifBaseService;
import fr.periscol.backend.service.dto.TarifBaseDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.TarifBase}.
 */
@RestController
@RequestMapping("/api")
public class TarifBaseResource {

    private final Logger log = LoggerFactory.getLogger(TarifBaseResource.class);

    private static final String ENTITY_NAME = "tarifBase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TarifBaseService tarifBaseService;

    private final TarifBaseRepository tarifBaseRepository;

    public TarifBaseResource(TarifBaseService tarifBaseService, TarifBaseRepository tarifBaseRepository) {
        this.tarifBaseService = tarifBaseService;
        this.tarifBaseRepository = tarifBaseRepository;
    }

    /**
     * {@code POST  /tarif-bases} : Create a new tarifBase.
     *
     * @param tarifBaseDTO the tarifBaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarifBaseDTO, or with status {@code 400 (Bad Request)} if the tarifBase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tarif-bases")
    public ResponseEntity<TarifBaseDTO> createTarifBase(@RequestBody TarifBaseDTO tarifBaseDTO) throws URISyntaxException {
        log.debug("REST request to save TarifBase : {}", tarifBaseDTO);
        if (tarifBaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarifBase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TarifBaseDTO result = tarifBaseService.save(tarifBaseDTO);
        return ResponseEntity
            .created(new URI("/api/tarif-bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tarif-bases/:id} : Updates an existing tarifBase.
     *
     * @param id the id of the tarifBaseDTO to save.
     * @param tarifBaseDTO the tarifBaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifBaseDTO,
     * or with status {@code 400 (Bad Request)} if the tarifBaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarifBaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tarif-bases/{id}")
    public ResponseEntity<TarifBaseDTO> updateTarifBase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TarifBaseDTO tarifBaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TarifBase : {}, {}", id, tarifBaseDTO);
        if (tarifBaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifBaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TarifBaseDTO result = tarifBaseService.save(tarifBaseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarifBaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tarif-bases/:id} : Partial updates given fields of an existing tarifBase, field will ignore if it is null
     *
     * @param id the id of the tarifBaseDTO to save.
     * @param tarifBaseDTO the tarifBaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifBaseDTO,
     * or with status {@code 400 (Bad Request)} if the tarifBaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarifBaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarifBaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tarif-bases/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TarifBaseDTO> partialUpdateTarifBase(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TarifBaseDTO tarifBaseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TarifBase partially : {}, {}", id, tarifBaseDTO);
        if (tarifBaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifBaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifBaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TarifBaseDTO> result = tarifBaseService.partialUpdate(tarifBaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarifBaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tarif-bases} : get all the tarifBases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tarifBases in body.
     */
    @GetMapping("/tarif-bases")
    public List<TarifBaseDTO> getAllTarifBases() {
        log.debug("REST request to get all TarifBases");
        return tarifBaseService.findAll();
    }

    /**
     * {@code GET  /tarif-bases/:id} : get the "id" tarifBase.
     *
     * @param id the id of the tarifBaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarifBaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tarif-bases/{id}")
    public ResponseEntity<TarifBaseDTO> getTarifBase(@PathVariable Long id) {
        log.debug("REST request to get TarifBase : {}", id);
        Optional<TarifBaseDTO> tarifBaseDTO = tarifBaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarifBaseDTO);
    }

    /**
     * {@code DELETE  /tarif-bases/:id} : delete the "id" tarifBase.
     *
     * @param id the id of the tarifBaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tarif-bases/{id}")
    public ResponseEntity<Void> deleteTarifBase(@PathVariable Long id) {
        log.debug("REST request to delete TarifBase : {}", id);
        tarifBaseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
