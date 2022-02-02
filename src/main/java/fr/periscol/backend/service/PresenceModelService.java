package fr.periscol.backend.service;

import fr.periscol.backend.domain.PresenceModel;
import fr.periscol.backend.repository.PresenceModelRepository;
import fr.periscol.backend.service.dto.PresenceModelDTO;
import fr.periscol.backend.service.mapper.PresenceModelMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PresenceModel}.
 */
@Service
@Transactional
public class PresenceModelService {

    private final Logger log = LoggerFactory.getLogger(PresenceModelService.class);

    private final PresenceModelRepository presenceModelRepository;

    private final PresenceModelMapper presenceModelMapper;

    public PresenceModelService(PresenceModelRepository presenceModelRepository, PresenceModelMapper presenceModelMapper) {
        this.presenceModelRepository = presenceModelRepository;
        this.presenceModelMapper = presenceModelMapper;
    }

    /**
     * Save a presenceModel.
     *
     * @param presenceModelDTO the entity to save.
     * @return the persisted entity.
     */
    public PresenceModelDTO save(PresenceModelDTO presenceModelDTO) {
        log.debug("Request to save PresenceModel : {}", presenceModelDTO);
        PresenceModel presenceModel = presenceModelMapper.toEntity(presenceModelDTO);
        presenceModel = presenceModelRepository.save(presenceModel);
        return presenceModelMapper.toDto(presenceModel);
    }

    /**
     * Partially update a presenceModel.
     *
     * @param presenceModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PresenceModelDTO> partialUpdate(PresenceModelDTO presenceModelDTO) {
        log.debug("Request to partially update PresenceModel : {}", presenceModelDTO);

        return presenceModelRepository
            .findById(presenceModelDTO.getId())
            .map(existingPresenceModel -> {
                presenceModelMapper.partialUpdate(existingPresenceModel, presenceModelDTO);

                return existingPresenceModel;
            })
            .map(presenceModelRepository::save)
            .map(presenceModelMapper::toDto);
    }

    /**
     * Get all the presenceModels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PresenceModelDTO> findAll() {
        log.debug("Request to get all PresenceModels");
        return presenceModelRepository.findAll().stream().map(presenceModelMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one presenceModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PresenceModelDTO> findOne(Long id) {
        log.debug("Request to get PresenceModel : {}", id);
        return presenceModelRepository.findById(id).map(presenceModelMapper::toDto);
    }

    /**
     * Delete the presenceModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PresenceModel : {}", id);
        presenceModelRepository.deleteById(id);
    }
}
