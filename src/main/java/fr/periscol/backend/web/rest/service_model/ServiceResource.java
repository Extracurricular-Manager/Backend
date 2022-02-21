package fr.periscol.backend.web.rest.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.service.dto.service_model.ModifiedServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.PresenceModelDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link PresenceModel}.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private static final String ENTITY_NAME = "service";

    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceMetadataService service;

    public ServiceResource(ServiceMetadataService service) {
        this.service = service;
    }

    /**
     * {@code POST  /service} : Create a new service.
     *
     * @param metadataDTO the metadata of the new service to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)}, or with status {@code 400 (Bad Request)} if a service has already the same name.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PresenceModelDTO> createService(
            @Valid @RequestBody NewServiceMetadataDTO metadataDTO) throws URISyntaxException {
        log.debug("REST request to create a new service : {}", metadataDTO.getName());

        final var dto = service.createServiceMetadata(metadataDTO);

        return ResponseEntity
                .created(new URI("/api/service/" + dto.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dto.getId().toString()))
                .build();
    }


    /**
     * {@code PATCH  /service/:id} : Partial updates given fields of an existing serviceMetadata, field will ignore if it is null
     *
     * @param id               the id of the serviceMetadataDTO to save.
     * @param serviceMetadataDTO the serviceMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the serviceMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceMetadataDTO is not found,
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service/{id}", consumes = {"application/json", "application/merge-patch+json"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceMetadataDTO> partialUpdateService(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ModifiedServiceMetadataDTO serviceMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceMetadata partially : {}, {}", id, serviceMetadataDTO);

        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }

        Optional<ServiceMetadataDTO> result = service.partialUpdate(id, serviceMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString())
        );
    }

    /**
     * {@code GET  /services} : get all the services metadata.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metadata services in body.
     */
    @GetMapping("/services")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceMetadataDTO> getAllServices() {
        log.debug("REST request to get all metadata services");
        return service.findAll();
    }

    /**
     * {@code GET  /service/:id} : get the "id" metadata service.
     *
     * @param id the id of the ServiceMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ServiceMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceMetadataDTO> getService(@PathVariable Long id) {
        log.debug("REST request to get ServiceMetadata : {}", id);
        Optional<ServiceMetadataDTO> serviceMetadataDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceMetadataDTO);
    }

    /**
     * //TODO Supprimer également les données en plus des méta-données ?
     * {@code DELETE  /service/:id} : delete the "id" metadata service.
     *
     * @param id the id of the serviceMetadata to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.debug("REST request to delete ServiceMetadata : {}", id);
        service.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}
