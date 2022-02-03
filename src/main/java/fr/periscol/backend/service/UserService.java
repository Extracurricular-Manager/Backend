package fr.periscol.backend.service;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.dto.UserDTO;
import fr.periscol.backend.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a userCustom.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDTO save(UserDTO userDTO) {
        log.debug("Request to save UserCustom : {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Partially update a userCustom.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDTO> partialUpdate(UserDTO userDTO) {
        log.debug("Request to partially update UserCustom : {}", userDTO);

        return userRepository
            .findById(userDTO.getName())
            .map(existingUserCustom -> {
                userMapper.partialUpdate(existingUserCustom, userDTO);

                return existingUserCustom;
            })
            .map(userRepository::save)
            .map(userMapper::toDto);
    }

    /**
     * Get all the userCustoms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        log.debug("Request to get all UserCustoms");
        return userRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userCustoms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userRepository.findAllWithEagerRelationships(pageable).map(userMapper::toDto);
    }

    /**
     * Get one userCustom by id.
     *
     * @param name the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(String name) {
        log.debug("Request to get UserCustom : {}", name);
        return userRepository.findOneWithEagerRelationships(name).map(userMapper::toDto);
    }

    /**
     * Delete the userCustom by id.
     *
     * @param name the id of the entity.
     */
    public void delete(String name) {
        log.debug("Request to delete UserCustom : {}", name);
        userRepository.deleteById(name);
    }
}
