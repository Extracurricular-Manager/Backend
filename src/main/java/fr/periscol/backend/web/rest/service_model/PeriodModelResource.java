package fr.periscol.backend.web.rest.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.service.dto.service_model.NewPeriodModelDTO;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
import fr.periscol.backend.service.service_model.PeriodModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
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

    public PeriodModelResource(PeriodModelService periodModelService) {
        this.periodModelService = periodModelService;
    }

    /**
     * {@code POST  /period-service} : Put an entry to PeriodModel. If exist, update.
     *
     * @param date        the date of the entry to get
     * @param serviceId      the ID of the service where put the entry
     * @param periodModelDTO the periodModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodModelDTO, or with status {@code 400 (Bad Request)} if the periodModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/period-service/{id}")
    public ResponseEntity<PeriodModelDTO> putEntry(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                   @PathVariable("id") Long serviceId,
                                                   @RequestBody NewPeriodModelDTO periodModelDTO) throws URISyntaxException {
        log.debug("REST request to create a new entry in PeriodModel : {}", periodModelDTO);
        final PeriodModelDTO result = Optional.ofNullable(date)
                .map(d -> periodModelService.updateForDay(periodModelDTO, serviceId, d))
                .orElse(periodModelService.updateForToday(periodModelDTO, serviceId));

        return ResponseEntity
                .created(new URI("/api/period-service/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /period-services/:id} : get all the periodModels.
     *
     * @param date   the date of the entry to get
     * @param serviceId the ID of the service where put the entry
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodModels in body.
     */
    @GetMapping("/period-service/{id}")
    public ResponseEntity<List<PeriodModelDTO>> getAllEntries(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                              @PathVariable("id") Long serviceId) {
        log.debug("REST request to get all PeriodModel entries from {} services", serviceId);
        final var list = Optional.ofNullable(date).map(d -> periodModelService.findAllForDay(serviceId, d))
                .orElse(periodModelService.findAllForToday(serviceId));
        return ResponseEntity.ok(list);
    }

    /**
     * {@code GET  /period-service/:serviceId/:childId} : get the "childId" entry from "serviceId" service.
     *
     * @param date   the date of the entry to get
     * @param serviceId the id of the service where retrieve the entry
     * @param childId   the id of the child entry to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/period-service/{serviceId}/{childId}")
    public ResponseEntity<PeriodModelDTO> getTimeSlotModel(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                           @PathVariable Long serviceId,
                                                           @PathVariable Long childId) {
        log.debug("REST request to get a child entry {} from the service {}", childId, serviceId);
        Optional<PeriodModelDTO> periodModelDTO = Optional.ofNullable(date)
                .map(d -> periodModelService.findOneForDay(childId, serviceId, d))
                .orElse(periodModelService.findOneForToday(childId, serviceId));

        return ResponseUtil.wrapOrNotFound(periodModelDTO);
    }

    /**
     * {@code DELETE  /period-service/{serviceId}/{childId}} : delete the "childId" entry for the "serviceId".
     *
     * @param date   the date of the entry to get
     * @param serviceId the id of the service where delete the entry
     * @param childId   the id of the child entry to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/period-service/{serviceId}/{childId}")
    public ResponseEntity<Void> deleteTimeSlotModel(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                    @PathVariable Long serviceId,
                                                    @PathVariable Long childId) {
        log.debug("REST request to delete {} entry for service {}", serviceId, childId);
        Optional.ofNullable(date).ifPresentOrElse(d -> periodModelService.deleteForDay(childId, serviceId, d),
                () -> periodModelService.deleteForToday(childId, serviceId));

        return ResponseEntity
                .noContent()
                .build();
    }
}
