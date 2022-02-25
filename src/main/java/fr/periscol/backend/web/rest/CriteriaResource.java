package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.CriteriaRepository;
import fr.periscol.backend.service.CriteriaService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.CriteriaDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.tarification.Criteria}.
 */
@RestController
@RequestMapping("/api")
public class CriteriaResource {

    private final Logger log = LoggerFactory.getLogger(CriteriaResource.class);

    private static final String ENTITY_NAME = "criteria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CriteriaService criteriaService;

    private final CriteriaRepository criteriaRepository;

    public CriteriaResource(CriteriaService criteriaService, CriteriaRepository criteriaRepository) {
        this.criteriaService = criteriaService;
        this.criteriaRepository = criteriaRepository;
    }

    /**
     * {@code POST  /criteria} : Create a new criteria.
     *
     * @param criteriaDTO the criteriaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new criteriaDTO, or with status {@code 400 (Bad Request)} if the criteria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/criteria")
    public ResponseEntity<CriteriaDTO> createCriteria(@RequestBody CriteriaDTO criteriaDTO) throws URISyntaxException {
        log.debug("REST request to save Criteria : {}", criteriaDTO);
        if (criteriaDTO.getId() != null) {
            throw new BadRequestAlertException("A new criteria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CriteriaDTO result = criteriaService.save(criteriaDTO);
        return ResponseEntity
                .created(new URI("/api/criteria/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /criteria/:id} : Updates an existing criteria.
     *
     * @param id the id of the criteriaDTO to save.
     * @param criteriaDTO the criteriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criteriaDTO,
     * or with status {@code 400 (Bad Request)} if the criteriaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the criteriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/criteria/{id}")
    public ResponseEntity<CriteriaDTO> updateCriteria(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody CriteriaDTO criteriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Criteria : {}, {}", id, criteriaDTO);
        if (criteriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criteriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!criteriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CriteriaDTO result = criteriaService.save(criteriaDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, criteriaDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /criteria/:id} : Partial updates given fields of an existing criteria, field will ignore if it is null
     *
     * @param id the id of the criteriaDTO to save.
     * @param criteriaDTO the criteriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criteriaDTO,
     * or with status {@code 400 (Bad Request)} if the criteriaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the criteriaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the criteriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/criteria/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CriteriaDTO> partialUpdateCriteria(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody CriteriaDTO criteriaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Criteria partially : {}, {}", id, criteriaDTO);
        if (criteriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criteriaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!criteriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CriteriaDTO> result = criteriaService.partialUpdate(criteriaDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, criteriaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /criterias} : get all the criterias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of criterias in body.
     */
    @GetMapping("/criterias")
    public List<CriteriaDTO> getAllCriterias() {
        log.debug("REST request to get all Criterias");
        return criteriaService.findAll();
    }

    /**
     * {@code GET  /criteria/:id} : get the "id" criteria.
     *
     * @param id the id of the criteriaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the criteriaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/criteria/{id}")
    public ResponseEntity<CriteriaDTO> getCriteria(@PathVariable Long id) {
        log.debug("REST request to get Criteria : {}", id);
        Optional<CriteriaDTO> criteriaDTO = criteriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(criteriaDTO);
    }


    /**
     * {@code DELETE  /criteria/:id} : delete the "id" criteria.
     *
     * @param id the id of the criteriaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/criteria/{id}")
    public ResponseEntity<Void> deleteCriteria(@PathVariable Long id) {
        log.debug("REST request to delete Criteria : {}", id);
        criteriaService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
