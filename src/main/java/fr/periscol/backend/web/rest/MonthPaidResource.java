package fr.periscol.backend.web.rest;

import fr.periscol.backend.domain.csvUtil.CsvUtil;
import fr.periscol.backend.repository.MonthPaidRepository;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.MonthPaidDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.service_model.ServiceMetadataMapper;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import fr.periscol.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing {@link fr.periscol.backend.domain.MonthPaid}.
 */
@RestController
@RequestMapping("/api")
public class MonthPaidResource {

    private final Logger log = LoggerFactory.getLogger(MonthPaidResource.class);

    private static final String ENTITY_NAME = "monthPaid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthPaidService monthPaidService;

    private final MonthPaidRepository monthPaidRepository;

    private final ChildService childService;

    private final PeriodModelService periodService;

    private final ChildMapper childMapper;

    private final ServiceMetadataService serviceMetadata;

    private  final ServiceMetadataMapper serviceMetadataMapper;

    public MonthPaidResource(MonthPaidService monthPaidService, MonthPaidRepository monthPaidRepository, ChildService childService, PeriodModelService periodService, ChildMapper childMapper, ServiceMetadataService serviceMetadata, ServiceMetadataMapper serviceMetadataMapper) {
        this.monthPaidService = monthPaidService;
        this.monthPaidRepository = monthPaidRepository;
        this.childService = childService;
        this.periodService = periodService;
        this.childMapper = childMapper;
        this.serviceMetadata = serviceMetadata;
        this.serviceMetadataMapper = serviceMetadataMapper;
    }

    /**
     * {@code POST  /monthPaid} : Create a new monthPaid.
     *
     * @param monthPaidDTO the monthPaidDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monthPaidDTO, or with status {@code 400 (Bad Request)} if the monthPaid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monthPaid")
    public ResponseEntity<MonthPaidDTO> createMonthPaid(@RequestBody MonthPaidDTO monthPaidDTO) throws URISyntaxException {
        log.debug("REST request to save MonthPaid : {}", monthPaidDTO);
        if (monthPaidDTO.getId() != null) {
            throw new BadRequestAlertException("A new monthPaid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MonthPaidDTO result = monthPaidService.save(monthPaidDTO);
        return ResponseEntity
            .created(new URI("/api/monthPaid/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /monthPaid/childAndDate/:child_id/:date} : get the monthPaid by child id and date.
     *
     * @param child_id of the monthpaid to retrieve.
     * @param date of the monthpaid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthPaidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthPaid/childAndDate/{child_id}/{date}")
    public ResponseEntity<MonthPaidDTO> getMonthPaidByChildAndDate(@PathVariable Long child_id,
                                                                   @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        log.debug("REST request to get MonthPaid : {}", child_id, date);
        Optional<MonthPaidDTO> monthPaidDTO = monthPaidService.findOneByChildAndDate(child_id,date);
        return ResponseUtil.wrapOrNotFound(monthPaidDTO);
    }

    /**
     * {@code GET  /monthPaid/child/:child_id} : get the monthPaid by child id.
     *
     * @param child_id of the monthpaid to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthPaidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthPaid/child/{child_id}")
    public List<MonthPaidDTO> getMonthPaidByChild(@PathVariable Long child_id) {
        log.debug("REST request to get MonthPaid : {}", child_id);
        return monthPaidService.findAllByChild(child_id);
    }


    /**
     * {@code PUT  /monthPaid/:id} : Updates an existing monthPaid.
     *
     * @param id the id of the monthPaidDTO to save.
     * @param monthPaidDTO the monthPaidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthPaidDTO,
     * or with status {@code 400 (Bad Request)} if the monthPaidDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monthPaidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monthPaid/{id}")
    public ResponseEntity<MonthPaidDTO> updateMonthPaid(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthPaidDTO monthPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MonthPaid : {}, {}", id, monthPaidDTO);
        if (monthPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MonthPaidDTO result = monthPaidService.save(monthPaidDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monthPaidDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /monthPaid/:id} : Partial updates given fields of an existing monthPaid, field will ignore if it is null
     *
     * @param id the id of the monthPaidDTO to save.
     * @param monthPaidDTO the monthPaidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthPaidDTO,
     * or with status {@code 400 (Bad Request)} if the monthPaidDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monthPaidDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monthPaidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monthPaid/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonthPaidDTO> partialUpdateMonthPaid(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MonthPaidDTO monthPaidDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MonthPaid partially : {}, {}", id, monthPaidDTO);
        if (monthPaidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthPaidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthPaidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonthPaidDTO> result = monthPaidService.partialUpdate(monthPaidDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, monthPaidDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monthPaids} : get all the monthPaids.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthPaids in body.
     */
    @GetMapping("/monthPaids")
    public List<MonthPaidDTO> getAllMonthPaids() {
        log.debug("REST request to get all MonthPaids");
        return monthPaidService.findAll();
    }

    /**
     * {@code GET  /monthPaid/:id} : get the "id" monthPaid.
     *
     * @param id the id of the monthPaidDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthPaidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthPaid/{id}")
    public ResponseEntity<MonthPaidDTO> getMonthPaid(@PathVariable Long id) {
        log.debug("REST request to get MonthPaid : {}", id);
        Optional<MonthPaidDTO> monthPaidDTO = monthPaidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monthPaidDTO);
    }

    @GetMapping(value = "/monthPaid/child/{id}/getDetail", produces = "application/x-excel")
    public ResponseEntity getDetail(@PathVariable Long id, @RequestParam(name = "date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date){
        Optional<ChildDTO> optionalChildDTO = childService.findOne(id);
        if(optionalChildDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            ChildDTO childDTO = optionalChildDTO.get();
            try {
                CsvUtil csvUtil = new CsvUtil(monthPaidService, childMapper, periodService, serviceMetadata, serviceMetadataMapper);
                File file = csvUtil.createXlsx(date);
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=test.xlsx")
                        .contentType(MediaType.valueOf("application/x-excel")).body(new FileSystemResource(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * {@code DELETE  /monthPaid/:id} : delete the "id" monthPaid.
     *
     * @param id the id of the monthPaidDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monthPaid/{id}")
    public ResponseEntity<Void> deleteMonthPaid(@PathVariable Long id) {
        log.debug("REST request to delete MonthPaid : {}", id);
        monthPaidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
