package fr.periscol.backend.service;

import fr.periscol.backend.domain.TimeSlotModel;
import fr.periscol.backend.repository.TimeSlotModelRepository;
import fr.periscol.backend.service.dto.TimeSlotModelDTO;
import fr.periscol.backend.service.mapper.TimeSlotModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TimeSlotModel}.
 */
@Service
@Transactional
public class TimeSlotModelService {

    private final Logger log = LoggerFactory.getLogger(TimeSlotModelService.class);

    private final TimeSlotModelRepository timeSlotModelRepository;

    private final TimeSlotModelMapper timeSlotModelMapper;

    public TimeSlotModelService(TimeSlotModelRepository timeSlotModelRepository, TimeSlotModelMapper timeSlotModelMapper) {
        this.timeSlotModelRepository = timeSlotModelRepository;
        this.timeSlotModelMapper = timeSlotModelMapper;
    }

    /**
     * Save a timeSlotModel.
     *
     * @param timeSlotModelDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSlotModelDTO save(TimeSlotModelDTO timeSlotModelDTO) {
        log.debug("Request to save TimeSlotModel : {}", timeSlotModelDTO);
        TimeSlotModel timeSlotModel = timeSlotModelMapper.toEntity(timeSlotModelDTO);
        timeSlotModel = timeSlotModelRepository.save(timeSlotModel);
        return timeSlotModelMapper.toDto(timeSlotModel);
    }

    /**
     * Partially update a timeSlotModel.
     *
     * @param timeSlotModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimeSlotModelDTO> partialUpdate(TimeSlotModelDTO timeSlotModelDTO) {
        log.debug("Request to partially update TimeSlotModel : {}", timeSlotModelDTO);

        return timeSlotModelRepository
            .findById(timeSlotModelDTO.getId())
            .map(existingTimeSlotModel -> {
                timeSlotModelMapper.partialUpdate(existingTimeSlotModel, timeSlotModelDTO);

                return existingTimeSlotModel;
            })
            .map(timeSlotModelRepository::save)
            .map(timeSlotModelMapper::toDto);
    }

    /**
     * Get all the timeSlotModels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TimeSlotModelDTO> findAll() {
        log.debug("Request to get all TimeSlotModels");
        return timeSlotModelRepository.findAll().stream().map(timeSlotModelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one timeSlotModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeSlotModelDTO> findOne(Long id) {
        log.debug("Request to get TimeSlotModel : {}", id);
        return timeSlotModelRepository.findById(id).map(timeSlotModelMapper::toDto);
    }

    /**
     * Delete the timeSlotModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeSlotModel : {}", id);
        timeSlotModelRepository.deleteById(id);
    }
}
