package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.repository.TarificationRepository;
import fr.periscol.backend.service.TarificationService;
import fr.periscol.backend.service.dto.TarificationDTO;
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
 * REST controller for managing {@link Criteria}.
 */
@RestController
@RequestMapping("/api")
public class TarificationResource {

    private final Logger log = LoggerFactory.getLogger(TarificationResource.class);

    private static final String ENTITY_NAME = "tarification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TarificationService tarificationService;

    private final TarificationRepository tarificationRepository;

    public TarificationResource(TarificationService tarificationService, TarificationRepository tarificationRepository) {
        this.tarificationService = tarificationService;
        this.tarificationRepository = tarificationRepository;
    }

    /**
     * {@code POST  /tarifications} : Create a new tarification.
     *
     * @param tarificationDTO the tarificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarificationDTO, or with status {@code 400 (Bad Request)} if the tarification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tarifications")
    public ResponseEntity<TarificationDTO> createTarification(@RequestBody TarificationDTO tarificationDTO) throws URISyntaxException {
        log.debug("REST request to save Tarification : {}", tarificationDTO);
        if (tarificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TarificationDTO result = tarificationService.save(tarificationDTO);
        return ResponseEntity
            .created(new URI("/api/tarifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tarifications/:id} : Updates an existing tarification.
     *
     * @param id the id of the tarificationDTO to save.
     * @param tarificationDTO the tarificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarificationDTO,
     * or with status {@code 400 (Bad Request)} if the tarificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tarifications/{id}")
    public ResponseEntity<TarificationDTO> updateTarification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TarificationDTO tarificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tarification : {}, {}", id, tarificationDTO);
        if (tarificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TarificationDTO result = tarificationService.save(tarificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tarifications/:id} : Partial updates given fields of an existing tarification, field will ignore if it is null
     *
     * @param id the id of the tarificationDTO to save.
     * @param tarificationDTO the tarificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarificationDTO,
     * or with status {@code 400 (Bad Request)} if the tarificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tarifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TarificationDTO> partialUpdateTarification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TarificationDTO tarificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tarification partially : {}, {}", id, tarificationDTO);
        if (tarificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TarificationDTO> result = tarificationService.partialUpdate(tarificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tarifications} : get all the tarifications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tarifications in body.
     */
    @GetMapping("/tarifications")
    public List<TarificationDTO> getAllTarifications() {
        log.debug("REST request to get all Tarifications");
        return tarificationService.findAll();
    }

    /**
     * {@code GET  /tarifications/:id} : get the "id" tarification.
     *
     * @param id the id of the tarificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tarifications/{id}")
    public ResponseEntity<TarificationDTO> getTarification(@PathVariable Long id) {
        log.debug("REST request to get Tarification : {}", id);
        Optional<TarificationDTO> tarificationDTO = tarificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarificationDTO);
    }

    /**
     * {@code DELETE  /tarifications/:id} : delete the "id" tarification.
     *
     * @param id the id of the tarificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tarifications/{id}")
    public ResponseEntity<Void> deleteTarification(@PathVariable Long id) {
        log.debug("REST request to delete Tarification : {}", id);
        tarificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
