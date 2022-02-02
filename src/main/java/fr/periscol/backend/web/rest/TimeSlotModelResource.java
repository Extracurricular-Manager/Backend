package fr.periscol.backend.web.rest;

import fr.periscol.backend.repository.TimeSlotModelRepository;
import fr.periscol.backend.service.TimeSlotModelService;
import fr.periscol.backend.service.dto.TimeSlotModelDTO;
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
 * REST controller for managing {@link fr.periscol.backend.domain.TimeSlotModel}.
 */
@RestController
@RequestMapping("/api")
public class TimeSlotModelResource {

    private final Logger log = LoggerFactory.getLogger(TimeSlotModelResource.class);

    private static final String ENTITY_NAME = "timeSlotModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeSlotModelService timeSlotModelService;

    private final TimeSlotModelRepository timeSlotModelRepository;

    public TimeSlotModelResource(TimeSlotModelService timeSlotModelService, TimeSlotModelRepository timeSlotModelRepository) {
        this.timeSlotModelService = timeSlotModelService;
        this.timeSlotModelRepository = timeSlotModelRepository;
    }

    /**
     * {@code POST  /time-slot-models} : Create a new timeSlotModel.
     *
     * @param timeSlotModelDTO the timeSlotModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeSlotModelDTO, or with status {@code 400 (Bad Request)} if the timeSlotModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-slot-models")
    public ResponseEntity<TimeSlotModelDTO> createTimeSlotModel(@RequestBody TimeSlotModelDTO timeSlotModelDTO) throws URISyntaxException {
        log.debug("REST request to save TimeSlotModel : {}", timeSlotModelDTO);
        if (timeSlotModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeSlotModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeSlotModelDTO result = timeSlotModelService.save(timeSlotModelDTO);
        return ResponseEntity
            .created(new URI("/api/time-slot-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /time-slot-models/:id} : Updates an existing timeSlotModel.
     *
     * @param id the id of the timeSlotModelDTO to save.
     * @param timeSlotModelDTO the timeSlotModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSlotModelDTO,
     * or with status {@code 400 (Bad Request)} if the timeSlotModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeSlotModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-slot-models/{id}")
    public ResponseEntity<TimeSlotModelDTO> updateTimeSlotModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeSlotModelDTO timeSlotModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeSlotModel : {}, {}", id, timeSlotModelDTO);
        if (timeSlotModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSlotModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSlotModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TimeSlotModelDTO result = timeSlotModelService.save(timeSlotModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeSlotModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /time-slot-models/:id} : Partial updates given fields of an existing timeSlotModel, field will ignore if it is null
     *
     * @param id the id of the timeSlotModelDTO to save.
     * @param timeSlotModelDTO the timeSlotModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSlotModelDTO,
     * or with status {@code 400 (Bad Request)} if the timeSlotModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeSlotModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeSlotModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-slot-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimeSlotModelDTO> partialUpdateTimeSlotModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TimeSlotModelDTO timeSlotModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeSlotModel partially : {}, {}", id, timeSlotModelDTO);
        if (timeSlotModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSlotModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSlotModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeSlotModelDTO> result = timeSlotModelService.partialUpdate(timeSlotModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeSlotModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /time-slot-models} : get all the timeSlotModels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeSlotModels in body.
     */
    @GetMapping("/time-slot-models")
    public List<TimeSlotModelDTO> getAllTimeSlotModels() {
        log.debug("REST request to get all TimeSlotModels");
        return timeSlotModelService.findAll();
    }

    /**
     * {@code GET  /time-slot-models/:id} : get the "id" timeSlotModel.
     *
     * @param id the id of the timeSlotModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSlotModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-slot-models/{id}")
    public ResponseEntity<TimeSlotModelDTO> getTimeSlotModel(@PathVariable Long id) {
        log.debug("REST request to get TimeSlotModel : {}", id);
        Optional<TimeSlotModelDTO> timeSlotModelDTO = timeSlotModelService.findOne(id);
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
        timeSlotModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
