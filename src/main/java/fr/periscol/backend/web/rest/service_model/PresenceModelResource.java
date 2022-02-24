package fr.periscol.backend.web.rest.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.service.dto.service_model.NewPresenceModelDTO;
import fr.periscol.backend.service.dto.service_model.PresenceModelDTO;
import fr.periscol.backend.service.service_model.PresenceModelService;
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
public class PresenceModelResource {

    private final Logger log = LoggerFactory.getLogger(PresenceModelResource.class);

    private static final String ENTITY_NAME = "presenceModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresenceModelService presenceModelService;

    public PresenceModelResource(PresenceModelService presenceModelService) {
        this.presenceModelService = presenceModelService;
    }

    /**
     * {@code POST  /presence-service} : Put an entry to PresenceModel. If exist, update.
     *
     * @param date        the date of the entry to get
     * @param serviceId      the ID of the service where put the entry
     * @param presenceModelDTO the presenceModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presenceModelDTO, or with status {@code 400 (Bad Request)} if the presenceModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presence-service/{id}")
    public ResponseEntity<PresenceModelDTO> putEntry(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                   @PathVariable("id") Long serviceId,
                                                   @RequestBody NewPresenceModelDTO presenceModelDTO) throws URISyntaxException {
        log.debug("REST request to create a new entry in PeriodModel : {}", presenceModelDTO);
        final PresenceModelDTO result = Optional.ofNullable(date)
                .map(d -> presenceModelService.updateForDay(presenceModelDTO, serviceId, d))
                .orElse(presenceModelService.updateForToday(presenceModelDTO, serviceId));

        return ResponseEntity
                .created(new URI("/api/presence-service/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /presence-services/:id} : get all the presenceModels.
     *
     * @param date   the date of the entry to get
     * @param serviceId the ID of the service where put the entry
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presenceModels in body.
     */
    @GetMapping("/presence-service/{id}")
    public ResponseEntity<List<PresenceModelDTO>> getAllEntries(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                              @PathVariable("id") Long serviceId) {
        log.debug("REST request to get all PresenceModel entries from {} services", serviceId);
        final var list = Optional.ofNullable(date).map(d -> presenceModelService.findAllForDay(serviceId, d))
                .orElse(presenceModelService.findAllForToday(serviceId));
        return ResponseEntity.ok(list);
    }

    /**
     * {@code GET  /presence-service/:serviceId/:childId} : get the "childId" entry from "serviceId" service.
     *
     * @param date   the date of the entry to get
     * @param serviceId the id of the service where retrieve the entry
     * @param childId   the id of the child entry to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presenceModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presence-service/{serviceId}/{childId}")
    public ResponseEntity<PresenceModelDTO> getTimeSlotModel(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                           @PathVariable Long serviceId,
                                                           @PathVariable Long childId) {
        log.debug("REST request to get a child entry {} from the service {}", childId, serviceId);
        Optional<PresenceModelDTO> presenceModelDTO = Optional.ofNullable(date)
                .map(d -> presenceModelService.findOneForDay(childId, serviceId, d))
                .orElse(presenceModelService.findOneForToday(childId, serviceId));

        return ResponseUtil.wrapOrNotFound(presenceModelDTO);
    }

    /**
     * {@code DELETE  /presence-service/{serviceId}/{childId}} : delete the "childId" entry for the "serviceId".
     *
     * @param date   the date of the entry to get
     * @param serviceId the id of the service where delete the entry
     * @param childId   the id of the child entry to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presence-service/{serviceId}/{childId}")
    public ResponseEntity<Void> deleteTimeSlotModel(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                                    @PathVariable Long serviceId,
                                                    @PathVariable Long childId) {
        log.debug("REST request to delete {} entry for service {}", serviceId, childId);
        Optional.ofNullable(date).ifPresentOrElse(d -> presenceModelService.deleteForDay(childId, serviceId, d),
                () -> presenceModelService.deleteForToday(childId, serviceId));

        return ResponseEntity
                .noContent()
                .build();
    }
}
