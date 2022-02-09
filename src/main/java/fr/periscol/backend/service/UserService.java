package fr.periscol.backend.service;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.dto.NewUserDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.dto.RoleNameDTO;
import fr.periscol.backend.service.dto.UserDTO;
import fr.periscol.backend.service.mapper.UserMapper;
import fr.periscol.backend.web.rest.errors.LoginAlreadyUsedException;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import fr.periscol.backend.web.rest.vm.PasswordVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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
            .findById(userDTO.getLogin())
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
        return findOneUser(name).map(userMapper::toDto);
    }

    private Optional<User> findOneUser(String name) {
        return userRepository.findOneWithEagerRelationships(name);
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


    /**
     * Get all roles of a User
     *
     * @param name the name of the entity
     * @return the list of all roles of the entity
     */
    public List<RoleDTO> getAllRoles(String name) {
        log.debug("Request to get all roles of user : {}", name);
        return findOne(name).map(UserDTO::getRoles).map(ArrayList::new).orElse(new ArrayList<>());
    }


    /**
     * Remove a role from a User
     *
     * @param name the name of the entity
     */
    public void deleteRole(String name, String role) {
        log.debug("Request to remove a from user : {}", name);
        final var userOpt = findOne(name);
        if(userOpt.isPresent()) {
            final var user = userOpt.get();
            user.setRoles(user.getRoles()
                    .stream()
                    .filter(r -> !r.getName().equals(role))
                    .collect(Collectors.toSet())
            );
            partialUpdate(user);
        }
    }

    /**
     * @param name the name of the user to add the role
     * @param roleName the name of the role
     * @return true if the role is added, false if the role is already present for this user
     */
    public boolean addRole(String name, RoleNameDTO roleName) {
        log.debug("Request to add a role to a user : {}", name);
        final var roleOpt = roleService.findOne(roleName.getName());
        final var userOpt = findOne(name);
        if(roleOpt.isPresent() && userOpt.isPresent()) {
            final var role = roleOpt.get();
            final var user = userOpt.get();
            if(!user.getRoles().contains(role)) {
                final var roles = new HashSet<>(user.getRoles());
                roles.add(role);
                final var newUser = new UserDTO();
                newUser.setRoles(roles);
                newUser.setLogin(name);
                partialUpdate(newUser);
                return true;
            } else
                return false;
        } else if(roleOpt.isEmpty())
            throw new NotFoundAlertException("Specified role does not exist.");
        else
            throw new NotFoundAlertException("Specified user does not exist.");
    }


    /**
     * Save a new User.
     *
     * @param newUser the entity to save.
     * @return the persisted entity.
     */
    public UserDTO createNewUser(NewUserDTO newUser) {
        log.debug("Request to create new User : {}", newUser);
        final var existingUser = findOne(newUser.getName());
        if(existingUser.isPresent())
            throw new LoginAlreadyUsedException();

        var user = userMapper.toUser(newUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }


    public void resetPassword(String name, PasswordVM password) {
        log.debug("Request to change password of a User : {}", name);
        final var userOpt = findOneUser(name);
        if(userOpt.isPresent()) {
            final var user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password.getPassword()));
            user.setActivated(false);
            userRepository.save(user);
        } else
            throw new NotFoundAlertException("User not found");
    }


    public void changePassword(String name, PasswordVM password) {
        log.debug("Request to change password of a User : {}", name);
        final var userOpt = findOneUser(name);
        if(userOpt.isPresent()) {
            final var user = userOpt.get();
            user.setPassword(passwordEncoder.encode(password.getPassword()));
            user.setActivated(true);
            userRepository.save(user);
        } else
            throw new NotFoundAlertException("User not found");
    }
}
