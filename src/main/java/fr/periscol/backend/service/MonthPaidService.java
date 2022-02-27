package fr.periscol.backend.service;

import fr.periscol.backend.repository.MonthPaidRepository;
import fr.periscol.backend.service.dto.MonthPaidDTO;
import fr.periscol.backend.service.mapper.MonthPaidMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link fr.periscol.backend.domain.MonthPaid}.
 */
@Service
@Transactional
public class MonthPaidService {

    private final Logger log = LoggerFactory.getLogger(MonthPaidService.class);

    private final MonthPaidRepository monthPaidRepository;

    private final MonthPaidMapper monthPaidMapper;

    public MonthPaidService(MonthPaidRepository monthPaidRepository, MonthPaidMapper monthPaidMapper) {
        this.monthPaidRepository = monthPaidRepository;
        this.monthPaidMapper = monthPaidMapper;
    }

    /**
     * Save a monthPaid.
     *
     * @param monthPaidDTO the entity to save.
     * @return the persisted entity.
     */
    public MonthPaidDTO save(MonthPaidDTO monthPaidDTO) {
        log.debug("Request to save MonthPaid : {}", monthPaidDTO);
        fr.periscol.backend.domain.MonthPaid monthPaid = monthPaidMapper.toEntity(monthPaidDTO);
        monthPaid = monthPaidRepository.save(monthPaid);
        return monthPaidMapper.toDto(monthPaid);
    }

    /**
     * Partially update a monthPaid.
     *
     * @param monthPaidDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonthPaidDTO> partialUpdate(MonthPaidDTO monthPaidDTO) {
        log.debug("Request to partially update MonthPaid : {}", monthPaidDTO);

        return monthPaidRepository
            .findById(monthPaidDTO.getId())
            .map(existingMonthPaid -> {
                monthPaidMapper.partialUpdate(existingMonthPaid, monthPaidDTO);

                return existingMonthPaid;
            })
            .map(monthPaidRepository::save)
            .map(monthPaidMapper::toDto);
    }

    /**
     * Get the child monthPaids by date.
     *
     * @return the one entities.
     */
    @Transactional(readOnly = true)
    public Optional<MonthPaidDTO> findOneByChildAndDate(Long childId, Date date) {
        log.debug("Request to get the child MonthPaid at a month");
        return monthPaidRepository.findOneByChildAndDate(childId, toLDT(date)).map(monthPaidMapper::toDto);
    }


    /**
     * Get all the monthPaids of a child.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MonthPaidDTO> findAllByChild(Long childId) {
        log.debug("Request to get all the child MonthPaid");
        return monthPaidRepository.findAllByChild(childId).stream()
                .map(monthPaidMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the monthPaids.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MonthPaidDTO> findAll() {
        log.debug("Request to get all MonthPaid");
        return monthPaidRepository.findAll().stream()
                .map(monthPaidMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     * Get one monthPaid by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonthPaidDTO> findOne(Long id) {
        log.debug("Request to get MonthPaid : {}", id);
        return monthPaidRepository.findById(id).map(monthPaidMapper::toDto);
    }


    /**
     * Delete the monthPaid by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MonthPaid : {}", id);
        monthPaidRepository.deleteById(id);
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
