package fr.periscol.backend.service;

import fr.periscol.backend.repository.MonthPaidRepository;
import fr.periscol.backend.service.dto.MonthPaidDTO;
import fr.periscol.backend.service.mapper.MonthPaidMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link fr.periscol.backend.domain.MonthPaid}.
 */
@Service
@Transactional
public class MonthPaid {

    private final Logger log = LoggerFactory.getLogger(MonthPaid.class);

    private final MonthPaidRepository monthPaidRepository;

    private final MonthPaidMapper monthPaidMapper;

    public MonthPaid(MonthPaidRepository monthPaidRepository, MonthPaidMapper monthPaidMapper) {
        this.monthPaidRepository = monthPaidRepository;
        this.monthPaidMapper = monthPaidMapper;
    }

    /**
     * Save a facturation.
     *
     * @param monthPaidDTO the entity to save.
     * @return the persisted entity.
     */
    public MonthPaidDTO save(MonthPaidDTO monthPaidDTO) {
        log.debug("Request to save Facturation : {}", monthPaidDTO);
        fr.periscol.backend.domain.MonthPaid monthPaid = monthPaidMapper.toEntity(monthPaidDTO);
        monthPaid = monthPaidRepository.save(monthPaid);
        return monthPaidMapper.toDto(monthPaid);
    }

    /**
     * Partially update a facturation.
     *
     * @param monthPaidDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MonthPaidDTO> partialUpdate(MonthPaidDTO monthPaidDTO) {
        log.debug("Request to partially update Facturation : {}", monthPaidDTO);

        return monthPaidRepository
            .findById(monthPaidDTO.getId())
            .map(existingFacturation -> {
                monthPaidMapper.partialUpdate(existingFacturation, monthPaidDTO);

                return existingFacturation;
            })
            .map(monthPaidRepository::save)
            .map(monthPaidMapper::toDto);
    }

    /**
     * Get all the facturations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MonthPaidDTO> findAll() {
        log.debug("Request to get all Facturations");
        return monthPaidRepository.findAll().stream().map(monthPaidMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one facturation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MonthPaidDTO> findOne(Long id) {
        log.debug("Request to get Facturation : {}", id);
        return monthPaidRepository.findById(id).map(monthPaidMapper::toDto);
    }

    /**
     * Delete the facturation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Facturation : {}", id);
        monthPaidRepository.deleteById(id);
    }

}
