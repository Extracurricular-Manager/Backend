package fr.periscol.backend.service;

import fr.periscol.backend.domain.UserCustom;
import fr.periscol.backend.repository.UserCustomRepository;
import fr.periscol.backend.service.dto.UserCustomDTO;
import fr.periscol.backend.service.mapper.UserCustomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link UserCustom}.
 */
@Service
@Transactional
public class UserCustomService {

    private final Logger log = LoggerFactory.getLogger(UserCustomService.class);

    private final UserCustomRepository userCustomRepository;

    private final UserCustomMapper userCustomMapper;

    public UserCustomService(UserCustomRepository userCustomRepository, UserCustomMapper userCustomMapper) {
        this.userCustomRepository = userCustomRepository;
        this.userCustomMapper = userCustomMapper;
    }

    /**
     * Save a userCustom.
     *
     * @param userCustomDTO the entity to save.
     * @return the persisted entity.
     */
    public UserCustomDTO save(UserCustomDTO userCustomDTO) {
        log.debug("Request to save UserCustom : {}", userCustomDTO);
        UserCustom userCustom = userCustomMapper.toEntity(userCustomDTO);
        userCustom = userCustomRepository.save(userCustom);
        return userCustomMapper.toDto(userCustom);
    }

    /**
     * Partially update a userCustom.
     *
     * @param userCustomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserCustomDTO> partialUpdate(UserCustomDTO userCustomDTO) {
        log.debug("Request to partially update UserCustom : {}", userCustomDTO);

        return userCustomRepository
            .findById(userCustomDTO.getId())
            .map(existingUserCustom -> {
                userCustomMapper.partialUpdate(existingUserCustom, userCustomDTO);

                return existingUserCustom;
            })
            .map(userCustomRepository::save)
            .map(userCustomMapper::toDto);
    }

    /**
     * Get all the userCustoms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserCustomDTO> findAll() {
        log.debug("Request to get all UserCustoms");
        return userCustomRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(userCustomMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userCustoms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserCustomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userCustomRepository.findAllWithEagerRelationships(pageable).map(userCustomMapper::toDto);
    }

    /**
     * Get one userCustom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserCustomDTO> findOne(Long id) {
        log.debug("Request to get UserCustom : {}", id);
        return userCustomRepository.findOneWithEagerRelationships(id).map(userCustomMapper::toDto);
    }

    /**
     * Delete the userCustom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserCustom : {}", id);
        userCustomRepository.deleteById(id);
    }
}
