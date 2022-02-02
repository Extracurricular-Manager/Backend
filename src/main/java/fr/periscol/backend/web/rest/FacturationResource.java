package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.FacturationRepository;
import fr.periscol.backend.service.FacturationService;
import fr.periscol.backend.service.dto.FacturationDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.Facturation}.
 */
@RestController
@RequestMapping("/api")
public class FacturationResource {

    private final Logger log = LoggerFactory.getLogger(FacturationResource.class);

    private static final String ENTITY_NAME = "facturation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturationService facturationService;

    private final FacturationRepository facturationRepository;

    public FacturationResource(FacturationService facturationService, FacturationRepository facturationRepository) {
        this.facturationService = facturationService;
        this.facturationRepository = facturationRepository;
    }

    /**
     * {@code POST  /facturations} : Create a new facturation.
     *
     * @param facturationDTO the facturationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturationDTO, or with status {@code 400 (Bad Request)} if the facturation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facturations")
    public ResponseEntity<FacturationDTO> createFacturation(@RequestBody FacturationDTO facturationDTO) throws URISyntaxException {
        log.debug("REST request to save Facturation : {}", facturationDTO);
        if (facturationDTO.getId() != null) {
            throw new BadRequestAlertException("A new facturation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturationDTO result = facturationService.save(facturationDTO);
        return ResponseEntity
            .created(new URI("/api/facturations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facturations/:id} : Updates an existing facturation.
     *
     * @param id the id of the facturationDTO to save.
     * @param facturationDTO the facturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturationDTO,
     * or with status {@code 400 (Bad Request)} if the facturationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facturations/{id}")
    public ResponseEntity<FacturationDTO> updateFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturationDTO facturationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facturation : {}, {}", id, facturationDTO);
        if (facturationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacturationDTO result = facturationService.save(facturationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facturationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facturations/:id} : Partial updates given fields of an existing facturation, field will ignore if it is null
     *
     * @param id the id of the facturationDTO to save.
     * @param facturationDTO the facturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturationDTO,
     * or with status {@code 400 (Bad Request)} if the facturationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facturations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturationDTO> partialUpdateFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturationDTO facturationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facturation partially : {}, {}", id, facturationDTO);
        if (facturationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturationDTO> result = facturationService.partialUpdate(facturationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facturationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facturations} : get all the facturations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturations in body.
     */
    @GetMapping("/facturations")
    public List<FacturationDTO> getAllFacturations() {
        log.debug("REST request to get all Facturations");
        return facturationService.findAll();
    }

    /**
     * {@code GET  /facturations/:id} : get the "id" facturation.
     *
     * @param id the id of the facturationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facturations/{id}")
    public ResponseEntity<FacturationDTO> getFacturation(@PathVariable Long id) {
        log.debug("REST request to get Facturation : {}", id);
        Optional<FacturationDTO> facturationDTO = facturationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturationDTO);
    }

    /**
     * {@code DELETE  /facturations/:id} : delete the "id" facturation.
     *
     * @param id the id of the facturationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facturations/{id}")
    public ResponseEntity<Void> deleteFacturation(@PathVariable Long id) {
        log.debug("REST request to delete Facturation : {}", id);
        facturationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
