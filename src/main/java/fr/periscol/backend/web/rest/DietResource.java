package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.DietRepository;
import fr.periscol.backend.service.DietService;
import fr.periscol.backend.service.dto.DietDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.Diet}.
 */
@RestController
@RequestMapping("/api")
public class DietResource {

    private final Logger log = LoggerFactory.getLogger(DietResource.class);

    private static final String ENTITY_NAME = "diet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietService dietService;

    private final DietRepository dietRepository;

    public DietResource(DietService dietService, DietRepository dietRepository) {
        this.dietService = dietService;
        this.dietRepository = dietRepository;
    }

    /**
     * {@code POST  /diets} : Create a new diet.
     *
     * @param dietDTO the dietDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietDTO, or with status {@code 400 (Bad Request)} if the diet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diets")
    public ResponseEntity<DietDTO> createDiet(@RequestBody DietDTO dietDTO) throws URISyntaxException {
        log.debug("REST request to save Diet : {}", dietDTO);
        if (dietDTO.getId() != null) {
            throw new BadRequestAlertException("A new diet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DietDTO result = dietService.save(dietDTO);
        return ResponseEntity
            .created(new URI("/api/diets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diets/:id} : Updates an existing diet.
     *
     * @param id the id of the dietDTO to save.
     * @param dietDTO the dietDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietDTO,
     * or with status {@code 400 (Bad Request)} if the dietDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diets/{id}")
    public ResponseEntity<DietDTO> updateDiet(@PathVariable(value = "id", required = false) final Long id, @RequestBody DietDTO dietDTO)
        throws URISyntaxException {
        log.debug("REST request to update Diet : {}, {}", id, dietDTO);
        if (dietDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DietDTO result = dietService.save(dietDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dietDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /diets/:id} : Partial updates given fields of an existing diet, field will ignore if it is null
     *
     * @param id the id of the dietDTO to save.
     * @param dietDTO the dietDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietDTO,
     * or with status {@code 400 (Bad Request)} if the dietDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dietDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dietDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DietDTO> partialUpdateDiet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DietDTO dietDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Diet partially : {}, {}", id, dietDTO);
        if (dietDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DietDTO> result = dietService.partialUpdate(dietDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dietDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /diets} : get all the diets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diets in body.
     */
    @GetMapping("/diets")
    public List<DietDTO> getAllDiets() {
        log.debug("REST request to get all Diets");
        return dietService.findAll();
    }

    /**
     * {@code GET  /diets/:id} : get the "id" diet.
     *
     * @param id the id of the dietDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diets/{id}")
    public ResponseEntity<DietDTO> getDiet(@PathVariable Long id) {
        log.debug("REST request to get Diet : {}", id);
        Optional<DietDTO> dietDTO = dietService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dietDTO);
    }

    /**
     * {@code DELETE  /diets/:id} : delete the "id" diet.
     *
     * @param id the id of the dietDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diets/{id}")
    public ResponseEntity<Void> deleteDiet(@PathVariable Long id) {
        log.debug("REST request to delete Diet : {}", id);
        dietService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
