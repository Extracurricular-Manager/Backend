package fr.periscol.backend.service;

import fr.periscol.backend.domain.Modifier;
import fr.periscol.backend.repository.ModifierRepository;
import fr.periscol.backend.service.dto.ModifierDTO;
import fr.periscol.backend.service.mapper.ModifierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Modifier}.
 */
@Service
@Transactional
public class ModifierService {

    private final Logger log = LoggerFactory.getLogger(ModifierService.class);

    private final ModifierRepository modifierRepository;

    private final ModifierMapper modifierMapper;

    public ModifierService(ModifierRepository modifierRepository, ModifierMapper modifierMapper) {
        this.modifierRepository = modifierRepository;
        this.modifierMapper = modifierMapper;
    }

    /**
     * Save a modifier.
     *
     * @param modifierDTO the entity to save.
     * @return the persisted entity.
     */
    public ModifierDTO save(ModifierDTO modifierDTO) {
        log.debug("Request to save Modifier : {}", modifierDTO);
        Modifier modifier = modifierMapper.toEntity(modifierDTO);
        modifier = modifierRepository.save(modifier);
        return modifierMapper.toDto(modifier);
    }

    /**
     * Partially update a modifier.
     *
     * @param modifierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModifierDTO> partialUpdate(ModifierDTO modifierDTO) {
        log.debug("Request to partially update Modifier : {}", modifierDTO);

        return modifierRepository
            .findById(modifierDTO.getId())
            .map(existingModifier -> {
                modifierMapper.partialUpdate(existingModifier, modifierDTO);

                return existingModifier;
            })
            .map(modifierRepository::save)
            .map(modifierMapper::toDto);
    }

    /**
     * Get all the modifiers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ModifierDTO> findAll() {
        log.debug("Request to get all Modifiers");
        return modifierRepository.findAll().stream().map(modifierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one modifier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModifierDTO> findOne(Long id) {
        log.debug("Request to get Modifier : {}", id);
        return modifierRepository.findById(id).map(modifierMapper::toDto);
    }

    /**
     * Delete the modifier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Modifier : {}", id);
        modifierRepository.deleteById(id);
    }
}
