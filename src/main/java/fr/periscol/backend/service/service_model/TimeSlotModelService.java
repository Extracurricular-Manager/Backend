package fr.periscol.backend.service.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.repository.service_model.PeriodRepository;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
import fr.periscol.backend.service.mapper.service_model.TimeSlotModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PeriodModel}.
 */
@Service
@Transactional
public class TimeSlotModelService {

    private final Logger log = LoggerFactory.getLogger(TimeSlotModelService.class);

    private final PeriodRepository periodRepository;

    private final TimeSlotModelMapper timeSlotModelMapper;

    public TimeSlotModelService(PeriodRepository periodRepository, TimeSlotModelMapper timeSlotModelMapper) {
        this.periodRepository = periodRepository;
        this.timeSlotModelMapper = timeSlotModelMapper;
    }

    /**
     * Save a timeSlotModel.
     *
     * @param periodModelDTO the entity to save.
     * @return the persisted entity.
     */
    public PeriodModelDTO save(PeriodModelDTO periodModelDTO) {
        log.debug("Request to save TimeSlotModel : {}", periodModelDTO);
        PeriodModel periodModel = timeSlotModelMapper.toEntity(periodModelDTO);
        periodModel = periodRepository.save(periodModel);
        return timeSlotModelMapper.toDto(periodModel);
    }

    /**
     * Partially update a timeSlotModel.
     *
     * @param periodModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeriodModelDTO> partialUpdate(PeriodModelDTO periodModelDTO) {
        log.debug("Request to partially update TimeSlotModel : {}", periodModelDTO);

        return periodRepository
            .findById(periodModelDTO.getId())
            .map(existingTimeSlotModel -> {
                timeSlotModelMapper.partialUpdate(existingTimeSlotModel, periodModelDTO);

                return existingTimeSlotModel;
            })
            .map(periodRepository::save)
            .map(timeSlotModelMapper::toDto);
    }

    /**
     * Get all the timeSlotModels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PeriodModelDTO> findAll() {
        log.debug("Request to get all TimeSlotModels");
        return periodRepository.findAll().stream().map(timeSlotModelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one timeSlotModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodModelDTO> findOne(Long id) {
        log.debug("Request to get TimeSlotModel : {}", id);
        return periodRepository.findById(id).map(timeSlotModelMapper::toDto);
    }

    /**
     * Delete the timeSlotModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeSlotModel : {}", id);
        periodRepository.deleteById(id);
    }
}
