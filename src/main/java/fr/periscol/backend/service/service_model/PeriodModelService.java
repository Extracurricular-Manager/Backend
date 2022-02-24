package fr.periscol.backend.service.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.repository.service_model.PeriodModelRepository;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.dto.service_model.NewPeriodModelDTO;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
import fr.periscol.backend.service.mapper.service_model.NewPeriodModelMapper;
import fr.periscol.backend.service.mapper.service_model.PeriodModelMapper;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PeriodModel}.
 */
@Service
@Transactional
public class PeriodModelService {

    private final Logger log = LoggerFactory.getLogger(PeriodModelService.class);

    private final PeriodModelRepository periodModelRepository;
    private final ServiceMetadataService metadataService;
    private final ChildService childService;
    private final PeriodModelMapper periodModelMapper;
    private final NewPeriodModelMapper newPeriodModelMapper;

    public PeriodModelService(PeriodModelRepository periodModelRepository, ServiceMetadataService metadataService, ChildService childService, PeriodModelMapper periodModelMapper, NewPeriodModelMapper newPeriodModelMapper) {
        this.periodModelRepository = periodModelRepository;
        this.metadataService = metadataService;
        this.childService = childService;
        this.periodModelMapper = periodModelMapper;
        this.newPeriodModelMapper = newPeriodModelMapper;
    }

    /**
     * Update a periodModel, create it if not exists.
     *
     * @param periodModelDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public PeriodModelDTO updateForToday(NewPeriodModelDTO periodModelDTO, Long serviceId) {
        return updateForDay(periodModelDTO, serviceId, Date.from(Instant.now()));
    }

    /**
     * Update a periodModel entry for a specific date
     * Entries have unicity on childId for a specific day and a specific service
     *
     * @param periodModelDTO the information to put un database
     * @param serviceId the ID of the service
     * @param date the date of the entry to update
     * @return the updated PeriodModelDTO
     */
    @Transactional
    public PeriodModelDTO updateForDay(NewPeriodModelDTO periodModelDTO, Long serviceId, Date date) {
        log.debug("Request to update the entry of the child {}, for the day {} from PeriodModel : {}", periodModelDTO.getChild().getId(), date, periodModelDTO);
        final var periodModel = periodModelRepository.findOneByDate(serviceId, periodModelDTO.getChild().getId(), toLDT(date), toLDT(Date.from(date.toInstant().plus(1 , ChronoUnit.DAYS))));

        recurrentCheckup(periodModelDTO.getChild().getId(), serviceId);

        if(periodModel.isPresent()) {
            periodModelMapper.partialUpdate(periodModel.get(), periodModelMapper.toDto(newPeriodModelMapper.toEntity(periodModelDTO)));
            return periodModelMapper.toDto(periodModelRepository.save(periodModel.get()));
        }
        return periodModelMapper.toDto(periodModelRepository.save(newPeriodModelMapper.toEntity(periodModelDTO)));
    }

    /**
     * Get all the periodModels for a specific service and a specific date.
     *
     * @param serviceId the id of the service
     * @param date the date
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodModelDTO> findAllForDay(Long serviceId, Date date) {
        log.debug("Request to get all PeriodModels for a specific day");

        recurrentCheckup(null, serviceId);

        return periodModelRepository.findAllByDate(serviceId, toLDT(date), toLDT(Date.from(date.toInstant().plus(1 , ChronoUnit.DAYS))))
                .stream().map(periodModelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the today periodModels for a specific service.
     *
     * @param serviceId the id of the service
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodModelDTO> findAllForToday(Long serviceId) {
        return findAllForDay(serviceId, Date.from(Instant.now()));
    }

    /**
     * Get one periodModel for a specific service and a specific date.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     * @param date the date
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodModelDTO> findOneForDay(Long childId, Long serviceId, Date date) {
        log.debug("Request to get the PeriodModel of child {} for service {} and day {}", childId, serviceId, date);
        recurrentCheckup(childId, serviceId);
        return periodModelRepository.findOneByDate(serviceId, childId, toLDT(date), toLDT(Date.from(date.toInstant().plus(1 , ChronoUnit.DAYS))))
                .map(periodModelMapper::toDto);
    }

    /**
     * Get one today periodModel for a specific service.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodModelDTO> findOneForToday(Long childId, Long serviceId) {
        return findOneForDay(childId, serviceId, Date.from(Instant.now()));
    }

    /**
     * Delete the periodModel by id.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     * @param date the date
     */
    public void deleteForDay(Long childId, Long serviceId, Date date) {
        log.debug("Request to delete the PeriodModel of child {} for service {} and day {}", childId, serviceId, date);
        recurrentCheckup(childId, serviceId);
        final var entry = findOneForDay(childId, serviceId, date);
        if(entry.isEmpty())
            throw new NotFoundAlertException("Child %d is not defined for service %d and day %s".formatted(childId, serviceId, date.toString()));

        periodModelRepository.deleteById(entry.get().getId());
    }

    /**
     * Delete the periodModel by id.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     */
    public void deleteForToday(Long childId, Long serviceId) {
        deleteForDay(childId, serviceId, Date.from(Instant.now()));
    }

    /**
     * Check if the ID exist
     * @param childId a child ID
     * @param serviceId a service ID
     */
    private void recurrentCheckup(@Nullable Long childId, @Nullable Long serviceId) {
        if(childId != null && childService.findOne(childId).isEmpty())
            throw new NotFoundAlertException("Child '%s' not doesn't exist.".formatted(childId));

        if(serviceId != null && metadataService.findOne(serviceId).isEmpty())
            throw new NotFoundAlertException("Service '%s' not doesn't exist.".formatted(serviceId));
    }

    /**
     * Convert a {@link Date} to a {@link LocalDateTime}
     * @param date a Date ton convert
     * @return the LocalDateTime
     */
    private LocalDateTime toLDT(Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

}
