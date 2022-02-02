package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.ServiceMetadata;
import fr.periscol.backend.repository.ServiceMetadataRepository;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link ServiceMetadata}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ServiceMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ServiceMetadataResource.class);

    private static final String ENTITY_NAME = "serviceMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceMetadataRepository serviceMetadataRepository;

    public ServiceMetadataResource(ServiceMetadataRepository serviceMetadataRepository) {
        this.serviceMetadataRepository = serviceMetadataRepository;
    }

    /**
     * {@code POST  /service-metadata} : Create a new serviceMetadata.
     *
     * @param serviceMetadata the serviceMetadata to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceMetadata, or with status {@code 400 (Bad Request)} if the serviceMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-metadata")
    public ResponseEntity<ServiceMetadata> createServiceMetadata(@RequestBody ServiceMetadata serviceMetadata) throws URISyntaxException {
        log.debug("REST request to save ServiceMetadata : {}", serviceMetadata);
        if (serviceMetadata.getId() != null) {
            throw new BadRequestAlertException("A new serviceMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceMetadata result = serviceMetadataRepository.save(serviceMetadata);
        return ResponseEntity
            .created(new URI("/api/service-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-metadata/:id} : Updates an existing serviceMetadata.
     *
     * @param id the id of the serviceMetadata to save.
     * @param serviceMetadata the serviceMetadata to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMetadata,
     * or with status {@code 400 (Bad Request)} if the serviceMetadata is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceMetadata couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-metadata/{id}")
    public ResponseEntity<ServiceMetadata> updateServiceMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceMetadata serviceMetadata
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceMetadata : {}, {}", id, serviceMetadata);
        if (serviceMetadata.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceMetadata.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceMetadata result = serviceMetadataRepository.save(serviceMetadata);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceMetadata.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-metadata/:id} : Partial updates given fields of an existing serviceMetadata, field will ignore if it is null
     *
     * @param id the id of the serviceMetadata to save.
     * @param serviceMetadata the serviceMetadata to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMetadata,
     * or with status {@code 400 (Bad Request)} if the serviceMetadata is not valid,
     * or with status {@code 404 (Not Found)} if the serviceMetadata is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceMetadata couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceMetadata> partialUpdateServiceMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceMetadata serviceMetadata
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceMetadata partially : {}, {}", id, serviceMetadata);
        if (serviceMetadata.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceMetadata.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceMetadata> result = serviceMetadataRepository
            .findById(serviceMetadata.getId())
            .map(existingServiceMetadata -> {
                if (serviceMetadata.getModule() != null) {
                    existingServiceMetadata.setModule(serviceMetadata.getModule());
                }
                if (serviceMetadata.getEndPoint() != null) {
                    existingServiceMetadata.setEndPoint(serviceMetadata.getEndPoint());
                }

                return existingServiceMetadata;
            })
            .map(serviceMetadataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceMetadata.getId().toString())
        );
    }

    /**
     * {@code GET  /service-metadata} : get all the serviceMetadata.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceMetadata in body.
     */
    @GetMapping("/service-metadata")
    public List<ServiceMetadata> getAllServiceMetadata(@RequestParam(required = false) String filter) {
        if ("model-is-null".equals(filter)) {
            log.debug("REST request to get all ServiceMetadatas where model is null");
            return StreamSupport
                .stream(serviceMetadataRepository.findAll().spliterator(), false)
                .filter(serviceMetadata -> serviceMetadata.getModel() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ServiceMetadata");
        return serviceMetadataRepository.findAll();
    }

    /**
     * {@code GET  /service-metadata/:id} : get the "id" serviceMetadata.
     *
     * @param id the id of the serviceMetadata to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceMetadata, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-metadata/{id}")
    public ResponseEntity<ServiceMetadata> getServiceMetadata(@PathVariable Long id) {
        log.debug("REST request to get ServiceMetadata : {}", id);
        Optional<ServiceMetadata> serviceMetadata = serviceMetadataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(serviceMetadata);
    }

    /**
     * {@code DELETE  /service-metadata/:id} : delete the "id" serviceMetadata.
     *
     * @param id the id of the serviceMetadata to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-metadata/{id}")
    public ResponseEntity<Void> deleteServiceMetadata(@PathVariable Long id) {
        log.debug("REST request to delete ServiceMetadata : {}", id);
        serviceMetadataRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
