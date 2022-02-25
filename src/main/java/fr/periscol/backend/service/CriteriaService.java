package fr.periscol.backend.service;

import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.repository.CriteriaRepository;
import fr.periscol.backend.service.dto.CriteriaDTO;
import fr.periscol.backend.service.mapper.CriteriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Criteria}.
 */
@Service
@Transactional
public class CriteriaService {

    private final Logger log = LoggerFactory.getLogger(CriteriaService.class);

    private final CriteriaRepository criteriaRepository;

    private final CriteriaMapper criteriaMapper;

    public CriteriaService(CriteriaRepository criteriaRepository, CriteriaMapper criteriaMapper) {
        this.criteriaRepository = criteriaRepository;
        this.criteriaMapper = criteriaMapper;
    }

    /**
     * Save a criteria.
     *
     * @param criteriaDTO the entity to save.
     * @return the persisted entity.
     */
    public CriteriaDTO save(CriteriaDTO criteriaDTO) {
        log.debug("Request to save Criteria : {}", criteriaDTO);
        Criteria criteria = criteriaMapper.toEntity(criteriaDTO);
        criteria = criteriaRepository.save(criteria);
        return criteriaMapper.toDto(criteria);
    }

    /**
     * Partially update a criteria.
     *
     * @param criteriaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CriteriaDTO> partialUpdate(CriteriaDTO criteriaDTO) {
        log.debug("Request to partially update Criteria : {}", criteriaDTO);

        return criteriaRepository
                .findById(criteriaDTO.getId())
                .map(existingCriteria -> {
                    criteriaMapper.partialUpdate(existingCriteria, criteriaDTO);

                    return existingCriteria;
                })
                .map(criteriaRepository::save)
                .map(criteriaMapper::toDto);
    }

    /**
     * Get all the families.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CriteriaDTO> findAll() {
        log.debug("Request to get all Families");
        return criteriaRepository.findAll().stream().map(criteriaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one criteria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CriteriaDTO> findOne(Long id) {
        log.debug("Request to get Criteria : {}", id);
        return criteriaRepository.findById(id).map(criteriaMapper::toDto);
    }

    /**
     * Delete the criteria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Criteria : {}", id);
        criteriaRepository.deleteById(id);
    }

}
