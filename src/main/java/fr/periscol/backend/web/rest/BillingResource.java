package fr.periscol.backend.web.rest;
import fr.periscol.backend.billing.BillingResponse;
import fr.periscol.backend.domain.csvUtil.CsvUtil;
import fr.periscol.backend.domain.tarification.TimePerspective;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.periscol.backend.billing.BillingCalculation}.
 */
@RestController
@RequestMapping("/api")
public class BillingResource {

    private final Logger log = LoggerFactory.getLogger(ClassroomResource.class);

    private final ChildService childService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BillingResource(ChildService childService) {
        this.childService = childService;
    }

    /**
     * {@code GET  /billing/:id?date=:date&temp=:temp} : get the total facturation of a child.
     *
     * @param id the id of the child facturaiton to retrieve.
     * @param date the date from which to count facturation
     * @param temporality the duration expected : year, month, week, day
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Json file, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/billing/{id}")
    public ResponseEntity<Object> getChild(@PathVariable Long id,
                                           @RequestParam(name = "date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                           @RequestParam(name = "temp") TimePerspective temporality) {
        log.debug("REST request to get Child billing {} from {} during {}", id, date, temporality);
        Optional<ChildDTO> optionalChildDTO = childService.findOne(id);

        if(optionalChildDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {

            BillingResponse billingResponse = new BillingResponse();
            billingResponse.setDaycareCost(1);
            billingResponse.setCanteenCost(1);
            billingResponse.setTotalCost(1);
            return ResponseEntity.ok(billingResponse);

        }

    }




}
