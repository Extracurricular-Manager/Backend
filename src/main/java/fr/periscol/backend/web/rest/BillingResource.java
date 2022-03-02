package fr.periscol.backend.web.rest;
import fr.periscol.backend.billing.BillingResponse;
import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.domain.tarification.TimePerspective;
import fr.periscol.backend.service.BillingCalculationService;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.service_model.ServiceMetadataMapper;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@RestController
@RequestMapping("/api")
public class BillingResource {

    private final Logger log = LoggerFactory.getLogger(ClassroomResource.class);

    private final ChildService childService;
    private final PeriodModelService periodService;
    private final MonthPaidService monthPaidService;
    private final ChildMapper childMapper;
    private final ServiceMetadataService serviceMetadataService;
    private final ServiceMetadataMapper serviceMetadataMapper;
    private final Calendar calendar = Calendar.getInstance();
    ZoneId defaultZoneId = ZoneId.systemDefault();



    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BillingResource(ChildService childService, PeriodModelService periodService, MonthPaidService monthPaidService, ChildMapper childMapper, ServiceMetadataService serviceMetadataService, ServiceMetadataMapper serviceMetadataMapper) {
        this.childService = childService;
        this.periodService = periodService;
        this.monthPaidService = monthPaidService;
        this.childMapper = childMapper;
        this.serviceMetadataService = serviceMetadataService;
        this.serviceMetadataMapper = serviceMetadataMapper;
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
    public ResponseEntity<Object> getChildBilling(@PathVariable Long id,
                                           @RequestParam(name = "date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                           @RequestParam(name = "temp") TimePerspective temporality) {
        log.debug("REST request to get Child billing {} from {} during {}", id, date, temporality);
        Optional<ChildDTO> optionalChildDTO = childService.findOne(id);

        if(optionalChildDTO.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            //variable that will hold the total cost billing result
            //type AtomicReference because variable used in lambda (multithread) and type assuring variable coherence
            AtomicReference<Float> totalCostAtomic = new AtomicReference<>(0f);
            //get the child
            Child child = childMapper.toEntity(optionalChildDTO.get());
            // get all the services
            List<ServiceMetadata> serviceMetadataList = serviceMetadataMapper.toEntity(serviceMetadataService.findAll());
            // prepare time interval to compute
            LocalDate localDate = Instant.ofEpochMilli(date.getTime())
                    .atZone(defaultZoneId)
                    .toLocalDate();
            LocalDate beginDate = localDate;
            LocalDate endDate = localDate;
            switch (temporality){
                case DAY :
                    break;
                case WEEK: beginDate = beginDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                           endDate = endDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                    break;
                case MONTH: beginDate = beginDate.with(TemporalAdjusters.firstDayOfMonth());
                            endDate = endDate.with(TemporalAdjusters.lastDayOfMonth());
                    break;
                case YEAR:  beginDate = beginDate.with(TemporalAdjusters.firstDayOfYear());
                            endDate = endDate.with(TemporalAdjusters.lastDayOfYear());
                    break;
                default:
                    break;
            }
            // get the billing calculation tool
            BillingCalculationService billingCalculationService = new BillingCalculationService(periodService);
            // start proper calculation
            beginDate.datesUntil(endDate).forEach(
                        d -> {
                            Date dateConverted = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                            for (ServiceMetadata service: serviceMetadataList
                                 ) {
                                totalCostAtomic.updateAndGet(v -> (float) (v + billingCalculationService.computeChildForDay(child, service, dateConverted)));
                            }
                        }
                    );

            float totalCost = totalCostAtomic.get();

            BillingResponse billingResponse = new BillingResponse();
//            billingResponse.setDaycareCost(1);
//            billingResponse.setCanteenCost(1);
            billingResponse.setTotalCost(totalCost);
            return ResponseEntity.ok(billingResponse);

        }

    }


}
