package fr.periscol.backend.service;

import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.repository.TarificationRepository;
import fr.periscol.backend.service.dto.TarificationDTO;
import fr.periscol.backend.service.mapper.TarificationMapper;
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
public class TarificationService {

    private final Logger log = LoggerFactory.getLogger(TarificationService.class);

    private final TarificationRepository tarificationRepository;

    private final TarificationMapper tarificationMapper;

    public TarificationService(TarificationRepository tarificationRepository, TarificationMapper tarificationMapper) {
        this.tarificationRepository = tarificationRepository;
        this.tarificationMapper = tarificationMapper;
    }

    /**
     * Save a tarification.
     *
     * @param tarificationDTO the entity to save.
     * @return the persisted entity.
     */
    public TarificationDTO save(TarificationDTO tarificationDTO) {
        log.debug("Request to save Tarification : {}", tarificationDTO);
        Criteria tarification = tarificationMapper.toEntity(tarificationDTO);
        tarification = tarificationRepository.save(tarification);
        return tarificationMapper.toDto(tarification);
    }

    /**
     * Partially update a tarification.
     *
     * @param tarificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TarificationDTO> partialUpdate(TarificationDTO tarificationDTO) {
        log.debug("Request to partially update Tarification : {}", tarificationDTO);

        return tarificationRepository
            .findById(tarificationDTO.getId())
            .map(existingTarification -> {
                tarificationMapper.partialUpdate(existingTarification, tarificationDTO);

                return existingTarification;
            })
            .map(tarificationRepository::save)
            .map(tarificationMapper::toDto);
    }

    /**
     * Get all the tarifications.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TarificationDTO> findAll() {
        log.debug("Request to get all Tarifications");
        return tarificationRepository.findAll().stream().map(tarificationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tarification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TarificationDTO> findOne(Long id) {
        log.debug("Request to get Tarification : {}", id);
        return tarificationRepository.findById(id).map(tarificationMapper::toDto);
    }

    /**
     * Delete the tarification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tarification : {}", id);
        tarificationRepository.deleteById(id);
    }
}
