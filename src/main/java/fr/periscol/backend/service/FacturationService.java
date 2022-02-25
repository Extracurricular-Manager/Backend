package fr.periscol.backend.service;

import fr.periscol.backend.domain.Facturation;
import fr.periscol.backend.repository.FacturationRepository;
import fr.periscol.backend.service.dto.FacturationDTO;
import fr.periscol.backend.service.mapper.FacturationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Facturation}.
 */
@Service
@Transactional
public class FacturationService {

    private final Logger log = LoggerFactory.getLogger(FacturationService.class);

    private final FacturationRepository facturationRepository;

    private final FacturationMapper facturationMapper;

    public FacturationService(FacturationRepository facturationRepository, FacturationMapper facturationMapper) {
        this.facturationRepository = facturationRepository;
        this.facturationMapper = facturationMapper;
    }

    /**
     * Save a facturation.
     *
     * @param facturationDTO the entity to save.
     * @return the persisted entity.
     */
    public FacturationDTO save(FacturationDTO facturationDTO) {
        log.debug("Request to save Facturation : {}", facturationDTO);
        Facturation facturation = facturationMapper.toEntity(facturationDTO);
        facturation = facturationRepository.save(facturation);
        return facturationMapper.toDto(facturation);
    }

    /**
     * Partially update a facturation.
     *
     * @param facturationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacturationDTO> partialUpdate(FacturationDTO facturationDTO) {
        log.debug("Request to partially update Facturation : {}", facturationDTO);

        return facturationRepository
            .findById(facturationDTO.getId())
            .map(existingFacturation -> {
                facturationMapper.partialUpdate(existingFacturation, facturationDTO);

                return existingFacturation;
            })
            .map(facturationRepository::save)
            .map(facturationMapper::toDto);
    }

    /**
     * Get all the facturations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FacturationDTO> findAll() {
        log.debug("Request to get all Facturations");
        return facturationRepository.findAll().stream().map(facturationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one facturation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacturationDTO> findOne(Long id) {
        log.debug("Request to get Facturation : {}", id);
        return facturationRepository.findById(id).map(facturationMapper::toDto);
    }

    /**
     * Delete the facturation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Facturation : {}", id);
        facturationRepository.deleteById(id);
    }

}
