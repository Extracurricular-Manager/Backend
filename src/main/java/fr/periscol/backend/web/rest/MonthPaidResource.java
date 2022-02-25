package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.MonthPaidRepository;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.MonthPaidDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.MonthPaid}.
 */
@RestController
@RequestMapping("/api")
public class MonthPaidResource {

    private final Logger log = LoggerFactory.getLogger(MonthPaidResource.class);

    private static final String ENTITY_NAME = "facturation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthPaidService monthPaidService;

    private final MonthPaidRepository monthPaidRepository;

    public MonthPaidResource(MonthPaidService monthPaidService, MonthPaidRepository monthPaidRepository) {
        this.monthPaidService = monthPaidService;
        this.monthPaidRepository = monthPaidRepository;
    }

    /**
     * {@code POST  /facturation} : Create a new facturation.
     *
     * @param monthPaidDTO the facturationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturationDTO, or with status {@code 400 (Bad Request)} if the facturation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facturation")
    public ResponseEntity<MonthPaidDTO> createFacturation(@RequestBody MonthPaidDTO monthPaidDTO) throws URISyntaxException {
        log.debug("REST request to save Facturation : {}", monthPaidDTO);
        if (monthPaidDTO.getId() != null) {
            throw new BadRequestAlertException("A new facturation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonthPaidDTO result = monthPaidService.save(monthPaidDTO);
        return ResponseEntity
            .created(new URI("/api/facturation/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facturation/:id} : Updates an existing facturation.
     *
     * @param id the id of the facturationDTO to save.
     * @param monthPaidDTO the facturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturationDTO,
     * or with status {@code 400 (Bad Request)} if the facturationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facturation/{id}")
    public ResponseEntity<MonthPaidDTO> updateFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthPaidDTO monthPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Facturation : {}, {}", id, monthPaidDTO);
        if (monthPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MonthPaidDTO result = monthPaidService.save(monthPaidDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monthPaidDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facturation/:id} : Partial updates given fields of an existing facturation, field will ignore if it is null
     *
     * @param id the id of the facturationDTO to save.
     * @param monthPaidDTO the facturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturationDTO,
     * or with status {@code 400 (Bad Request)} if the facturationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facturation/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonthPaidDTO> partialUpdateFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthPaidDTO monthPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facturation partially : {}, {}", id, monthPaidDTO);
        if (monthPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonthPaidDTO> result = monthPaidService.partialUpdate(monthPaidDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monthPaidDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facturations} : get all the facturations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturations in body.
     */
    @GetMapping("/facturations")
    public List<MonthPaidDTO> getAllFacturations() {
        log.debug("REST request to get all Facturations");
        return monthPaidService.findAll();
    }

    /**
     * {@code GET  /facturation/:id} : get the "id" facturation.
     *
     * @param id the id of the facturationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facturation/{id}")
    public ResponseEntity<MonthPaidDTO> getFacturation(@PathVariable Long id) {
        log.debug("REST request to get Facturation : {}", id);
        Optional<MonthPaidDTO> facturationDTO = monthPaidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facturationDTO);
    }

    /**
     * {@code DELETE  /facturation/:id} : delete the "id" facturation.
     *
     * @param id the id of the facturationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facturation/{id}")
    public ResponseEntity<Void> deleteFacturation(@PathVariable Long id) {
        log.debug("REST request to delete Facturation : {}", id);
        monthPaidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
