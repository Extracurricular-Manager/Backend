package fr.periscol.backend.service.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.repository.service_model.PresenceModelRepository;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.dto.service_model.NewPresenceModelDTO;
import fr.periscol.backend.service.dto.service_model.PresenceModelDTO;
import fr.periscol.backend.service.mapper.service_model.NewPresenceModelMapper;
import fr.periscol.backend.service.mapper.service_model.PresenceModelMapper;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PresenceModel}.
 */
@Service
@Transactional
public class PresenceModelService {

    private final Logger log = LoggerFactory.getLogger(PresenceModelService.class);

    private final PresenceModelRepository presenceModelRepository;
    private final ServiceMetadataService metadataService;
    private final ChildService childService;
    private final PresenceModelMapper presenceModelMapper;
    private final NewPresenceModelMapper newPresenceModelMapper;

    public PresenceModelService(PresenceModelRepository presenceModelRepository, ServiceMetadataService metadataService, ChildService childService, PresenceModelMapper presenceModelMapper, NewPresenceModelMapper newPresenceModelMapper) {
        this.presenceModelRepository = presenceModelRepository;
        this.metadataService = metadataService;
        this.childService = childService;
        this.presenceModelMapper = presenceModelMapper;
        this.newPresenceModelMapper = newPresenceModelMapper;
    }

    /**
     * Update a presenceModel, create it if not exists.
     *
     * @param presenceModelDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public PresenceModelDTO updateForToday(NewPresenceModelDTO presenceModelDTO, Long serviceId) {
        return updateForDay(presenceModelDTO, serviceId, Date.from(Instant.now()));
    }

    /**
     * Update a presenceModel entry for a specific date
     * Entries have unicity on childId for a specific day and a specific service
     *
     * @param presenceModelDTO the information to put un database
     * @param serviceId the ID of the service
     * @param date the date of the entry to update
     * @return the updated PresenceModelDTO
     */
    @Transactional
    public PresenceModelDTO updateForDay(NewPresenceModelDTO presenceModelDTO, Long serviceId, Date date) {
        log.debug("Request to update the entry of the child {}, for the day {} from PresenceModel : {}", presenceModelDTO.getChild().getId(), date, presenceModelDTO);
        final var presenceModel = presenceModelRepository.findOneByDate(serviceId, presenceModelDTO.getChild().getId(), toLD(date));

        recurrentCheckup(presenceModelDTO.getChild().getId(), serviceId);

        if(presenceModel.isPresent()) {
            presenceModelMapper.partialUpdate(presenceModel.get(), presenceModelMapper.toDto(newPresenceModelMapper.toEntity(presenceModelDTO)));
            return presenceModelMapper.toDto(presenceModelRepository.save(presenceModel.get()));
        }
        return presenceModelMapper.toDto(presenceModelRepository.save(newPresenceModelMapper.toEntity(presenceModelDTO)));
    }

    /**
     * Get all the presenceModels for a specific service and a specific date.
     *
     * @param serviceId the id of the service
     * @param date the date
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PresenceModelDTO> findAllForDay(Long serviceId, Date date) {
        log.debug("Request to get all PresenceModels for a specific day");

        recurrentCheckup(null, serviceId);

        return presenceModelRepository.findAllByDate(serviceId, toLD(date))
                .stream().map(presenceModelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the today presenceModels for a specific service.
     *
     * @param serviceId the id of the service
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PresenceModelDTO> findAllForToday(Long serviceId) {
        return findAllForDay(serviceId, Date.from(Instant.now()));
    }

    /**
     * Get one presenceModel for a specific service and a specific date.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     * @param date the date
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PresenceModelDTO> findOneForDay(Long childId, Long serviceId, Date date) {
        log.debug("Request to get the PresenceModel of child {} for service {} and day {}", childId, serviceId, date);
        recurrentCheckup(childId, serviceId);
        return presenceModelRepository.findOneByDate(serviceId, childId, toLD(date))
                .map(presenceModelMapper::toDto);
    }

    /**
     * Get one today presenceModel for a specific service.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     *
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PresenceModelDTO> findOneForToday(Long childId, Long serviceId) {
        return findOneForDay(childId, serviceId, Date.from(Instant.now()));
    }

    /**
     * Delete the presenceModel by id.
     *
     * @param childId the id of the child
     * @param serviceId the id of the service
     * @param date the date
     */
    public void deleteForDay(Long childId, Long serviceId, Date date) {
        log.debug("Request to delete the PresenceModel of child {} for service {} and day {}", childId, serviceId, date);
        recurrentCheckup(childId, serviceId);
        final var entry = findOneForDay(childId, serviceId, date);
        if(entry.isEmpty())
            throw new NotFoundAlertException("Child %d is not defined for service %d and day %s".formatted(childId, serviceId, date.toString()));

        presenceModelRepository.deleteById(entry.get().getId());
    }

    /**
     * Delete the presenceModel by id.
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
     * Convert a {@link Date} to a {@link LocalDate}
     * @param date a Date ton convert
     * @return the LocalDateTime
     */
    private LocalDate toLD(Date date) {
        return new Timestamp(date.getTime()).toLocalDateTime().toLocalDate();
    }
}
