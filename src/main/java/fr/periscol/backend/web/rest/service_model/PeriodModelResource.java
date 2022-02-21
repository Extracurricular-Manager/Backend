package fr.periscol.backend.web.rest.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.repository.service_model.PeriodRepository;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
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
 * REST controller for managing {@link PeriodModel}.
 */
@RestController
@RequestMapping("/api")
public class PeriodModelResource {

    private final Logger log = LoggerFactory.getLogger(PeriodModelResource.class);

    private static final String ENTITY_NAME = "periodModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodModelService periodModelService;
    private final PeriodRepository periodRepository;

    public PeriodModelResource(PeriodModelService periodModelService, PeriodRepository periodRepository) {
        this.periodModelService = periodModelService;
        this.periodRepository = periodRepository;
    }

    /**
     * {@code POST  /period} : Create a new timeSlotModel.
     *
     * @param periodModelDTO the timeSlotModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeSlotModelDTO, or with status {@code 400 (Bad Request)} if the timeSlotModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/period-service")
    public ResponseEntity<PeriodModelDTO> createPeriodModel(@RequestBody PeriodModelDTO periodModelDTO) throws URISyntaxException {
        log.debug("REST request to save PeriodModel : {}", periodModelDTO);
        PeriodModelDTO result = periodModelService.save(periodModelDTO);
        return ResponseEntity
            .created(new URI("/api/time-slot-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /time-slot-models/:id} : Updates an existing timeSlotModel.
     *
     * @param id the id of the timeSlotModelDTO to save.
     * @param periodModelDTO the timeSlotModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSlotModelDTO,
     * or with status {@code 400 (Bad Request)} if the timeSlotModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeSlotModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-slot-models/{id}")
    public ResponseEntity<PeriodModelDTO> updateTimeSlotModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeriodModelDTO periodModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeSlotModel : {}, {}", id, periodModelDTO);
        if (periodModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeriodModelDTO result = periodModelService.save(periodModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, periodModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /time-slot-models/:id} : Partial updates given fields of an existing timeSlotModel, field will ignore if it is null
     *
     * @param id the id of the timeSlotModelDTO to save.
     * @param periodModelDTO the timeSlotModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSlotModelDTO,
     * or with status {@code 400 (Bad Request)} if the timeSlotModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeSlotModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeSlotModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-slot-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeriodModelDTO> partialUpdateTimeSlotModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeriodModelDTO periodModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeSlotModel partially : {}, {}", id, periodModelDTO);
        if (periodModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeriodModelDTO> result = periodModelService.partialUpdate(periodModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, periodModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /time-slot-models} : get all the timeSlotModels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeSlotModels in body.
     */
    @GetMapping("/time-slot-models")
    public List<PeriodModelDTO> getAllTimeSlotModels() {
        log.debug("REST request to get all TimeSlotModels");
        return periodModelService.findAll();
    }

    /**
     * {@code GET  /time-slot-models/:id} : get the "id" timeSlotModel.
     *
     * @param id the id of the timeSlotModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSlotModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-slot-models/{id}")
    public ResponseEntity<PeriodModelDTO> getTimeSlotModel(@PathVariable Long id) {
        log.debug("REST request to get TimeSlotModel : {}", id);
        Optional<PeriodModelDTO> timeSlotModelDTO = periodModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeSlotModelDTO);
    }

    /**
     * {@code DELETE  /time-slot-models/:id} : delete the "id" timeSlotModel.
     *
     * @param id the id of the timeSlotModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-slot-models/{id}")
    public ResponseEntity<Void> deleteTimeSlotModel(@PathVariable Long id) {
        log.debug("REST request to delete TimeSlotModel : {}", id);
        periodModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
