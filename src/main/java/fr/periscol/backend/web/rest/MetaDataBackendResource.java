package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.MetaDataBackendService;
import fr.periscol.backend.service.dto.MetaDataBackendDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class MetaDataBackendResource {

    private final Logger log = LoggerFactory.getLogger(MetaDataBackendResource.class);

    private static final String ENTITY_NAME = "metaDataBackend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaDataBackendService metaDataBackendService;

    private final MetaDataBackendRepository metaDataBackendRepository;

    public MetaDataBackendResource(MetaDataBackendService metaDataBackendService, MetaDataBackendRepository metaDataBackendRepository) {
        this.metaDataBackendService = metaDataBackendService;
        this.metaDataBackendRepository = metaDataBackendRepository;
    }

    /**
     * {@code GET /info : get the Metadata of the backend.}
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metadata.
     */
    @GetMapping("/info")
    public MetaDataBackendDTO getMetaDataBackend(){
        MetaDataBackendDTO metaDataBackendDTO = metaDataBackendService.findIt();
        return metaDataBackendDTO;
    }

    /**
     * {@code PATCH /info : update the name of the school in the metaData of the backend.}
     *
     * @param nameOfSchool The new name of the school.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metadata.
     */
    @PatchMapping("/info")
    public ResponseEntity<MetaDataBackendDTO> patchSchoolName(@RequestBody String nameOfSchool){
        log.debug("REST request to update MetaDataBackend");
        MetaDataBackendDTO metaDataBackendDTO = metaDataBackendService.updateName(nameOfSchool);
        return ResponseEntity
                .ok().body(metaDataBackendDTO);
    }
}
