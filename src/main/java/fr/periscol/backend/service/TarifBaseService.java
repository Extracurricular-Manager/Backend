package fr.periscol.backend.service;

import fr.periscol.backend.domain.TarifBase;
import fr.periscol.backend.repository.TarifBaseRepository;
import fr.periscol.backend.service.dto.TarifBaseDTO;
import fr.periscol.backend.service.mapper.TarifBaseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TarifBase}.
 */
@Service
@Transactional
public class TarifBaseService {

    private final Logger log = LoggerFactory.getLogger(TarifBaseService.class);

    private final TarifBaseRepository tarifBaseRepository;

    private final TarifBaseMapper tarifBaseMapper;

    public TarifBaseService(TarifBaseRepository tarifBaseRepository, TarifBaseMapper tarifBaseMapper) {
        this.tarifBaseRepository = tarifBaseRepository;
        this.tarifBaseMapper = tarifBaseMapper;
    }

    /**
     * Save a tarifBase.
     *
     * @param tarifBaseDTO the entity to save.
     * @return the persisted entity.
     */
    public TarifBaseDTO save(TarifBaseDTO tarifBaseDTO) {
        log.debug("Request to save TarifBase : {}", tarifBaseDTO);
        TarifBase tarifBase = tarifBaseMapper.toEntity(tarifBaseDTO);
        tarifBase = tarifBaseRepository.save(tarifBase);
        return tarifBaseMapper.toDto(tarifBase);
    }

    /**
     * Partially update a tarifBase.
     *
     * @param tarifBaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TarifBaseDTO> partialUpdate(TarifBaseDTO tarifBaseDTO) {
        log.debug("Request to partially update TarifBase : {}", tarifBaseDTO);

        return tarifBaseRepository
            .findById(tarifBaseDTO.getId())
            .map(existingTarifBase -> {
                tarifBaseMapper.partialUpdate(existingTarifBase, tarifBaseDTO);

                return existingTarifBase;
            })
            .map(tarifBaseRepository::save)
            .map(tarifBaseMapper::toDto);
    }

    /**
     * Get all the tarifBases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TarifBaseDTO> findAll() {
        log.debug("Request to get all TarifBases");
        return tarifBaseRepository.findAll().stream().map(tarifBaseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tarifBase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TarifBaseDTO> findOne(Long id) {
        log.debug("Request to get TarifBase : {}", id);
        return tarifBaseRepository.findById(id).map(tarifBaseMapper::toDto);
    }

    /**
     * Delete the tarifBase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TarifBase : {}", id);
        tarifBaseRepository.deleteById(id);
    }
}
