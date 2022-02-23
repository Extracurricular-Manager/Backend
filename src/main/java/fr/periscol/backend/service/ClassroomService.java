package fr.periscol.backend.service;

import fr.periscol.backend.domain.Classroom;
import fr.periscol.backend.repository.ClassroomRepository;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.ClassroomDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.ClassroomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Classroom}.
 */
@Service
@Transactional
public class ClassroomService {

    private final Logger log = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    private final ClassroomMapper classroomMapper;

    private final ChildMapper childMapper;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper, ChildMapper childMapper) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
        this.childMapper = childMapper;
    }

    /**
     * Save a classroom.
     *
     * @param classroomDTO the entity to save.
     * @return the persisted entity.
     */
    public ClassroomDTO save(ClassroomDTO classroomDTO) {
        log.debug("Request to save Classroom : {}", classroomDTO);
        Classroom classroom = classroomMapper.toEntity(classroomDTO);
        classroom = classroomRepository.save(classroom);
        return classroomMapper.toDto(classroom);
    }

    /**
     * Partially update a classroom.
     *
     * @param classroomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClassroomDTO> partialUpdate(ClassroomDTO classroomDTO) {
        log.debug("Request to partially update Classroom : {}", classroomDTO);

        return classroomRepository
            .findById(classroomDTO.getId())
            .map(existingClassroom -> {
                classroomMapper.partialUpdate(existingClassroom, classroomDTO);

                return existingClassroom;
            })
            .map(classroomRepository::save)
            .map(classroomMapper::toDto);
    }

    /**
     * Get all the classrooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClassroomDTO> findAll() {
        log.debug("Request to get all Classrooms");
        return classroomRepository.findAll().stream().map(classroomMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClassroomDTO> findOne(Long id) {
        log.debug("Request to get Classroom : {}", id);
        return classroomRepository.findById(id).map(classroomMapper::toDto);
    }

    /**
     * Get the children of a classroom by its id.
     *
     * @return the list of children.
     */
    @Transactional(readOnly = true)
    public Optional<List<ChildDTO>> findAllChildren(Long id) {
        log.debug("Request to get all the children in the classroom with id : {}.", id);
        Optional<Classroom> optionalClassroomDTO = classroomRepository.findById(id);
        if (optionalClassroomDTO.isPresent()){
            List<ChildDTO> childDTOList = optionalClassroomDTO.get().getChildren().stream()
                    .map(childMapper::toDtoId).toList();
            return Optional.of(childDTOList);
        }
        return Optional.empty();
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Classroom : {}", id);
        classroomRepository.deleteById(id);
    }
}
