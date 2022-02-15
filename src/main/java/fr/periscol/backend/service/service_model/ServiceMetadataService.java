package fr.periscol.backend.service.service_model;

import fr.periscol.backend.domain.service_model.ModelEnum;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.repository.service_model.ServiceMetadataRepository;
import fr.periscol.backend.service.dto.service_model.ModifiedServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.service.mapper.service_model.ModifiedServiceMapper;
import fr.periscol.backend.service.mapper.service_model.NewServiceMetadataMapper;
import fr.periscol.backend.service.mapper.service_model.ServiceMetadataMapper;
import fr.periscol.backend.web.rest.errors.AlreadyExistAlertException;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceMetadataService {

    private final Logger log = LoggerFactory.getLogger(ServiceMetadataService.class);

    private final ServiceMetadataRepository repository;
    private final NewServiceMetadataMapper newMapper;
    private final ModifiedServiceMapper modifiedMapper;
    private final ServiceMetadataMapper mapper;

    public ServiceMetadataService(ServiceMetadataRepository repository, NewServiceMetadataMapper newMapper, ModifiedServiceMapper modifiedMapper, ServiceMetadataMapper mapper) {
        this.repository = repository;
        this.newMapper = newMapper;
        this.modifiedMapper = modifiedMapper;
        this.mapper = mapper;
    }


    @Transactional
    public ServiceMetadata createServiceMetadata(NewServiceMetadataDTO metadata) {
        final var existingEntity = repository.findOneByName(metadata.getName());
        if(existingEntity.isPresent())
            throw new AlreadyExistAlertException("A service with the same name already exists");

        if(!ModelEnum.exists(metadata.getModel()))
            throw new NotFoundAlertException("Model does not exist. Existing models: " +
                    Arrays.stream(ModelEnum.values()).map(ModelEnum::getId).collect(Collectors.joining(", ")));

        return repository.save(newMapper.toEntity(metadata));
    }

    /**
     * Save a serviceMetadata.
     *
     * @param metadataDto the entity to save.
     * @return the persisted entity.
     */
    public ServiceMetadataDTO save(NewServiceMetadataDTO metadataDto) {
        log.debug("Request to save ServiceMetadata : {}", metadataDto);
        ServiceMetadata metadata = newMapper.toEntity(metadataDto);
        metadata = repository.save(metadata);
        return mapper.toDto(metadata);
    }

    /**
     * Get one service metadata by id.
     *
     * @param name the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceMetadataDTO> findOne(Long name) {
        log.debug("Request to get ServiceMetadata : {}", name);
        return repository.findById(name).map(mapper::toDto);
    }

    /**
     * Get all the serviceMetadata.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ServiceMetadataDTO> findAll() {
        log.debug("Request to get all ServiceMetadata");
        return repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Delete the metadata service by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceMetadata : {}", id);
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new NotFoundAlertException("'%s' doesn't exist".formatted(id));
        }
    }

    /**
     * Partially update a userCustom.
     *
     * @param metadata the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceMetadataDTO> partialUpdate(Long id, ModifiedServiceMetadataDTO metadata) {
        log.debug("Request to partially update ServiceMetadata : {}", id);

        return repository
                .findById(id)
                .map(existingUserCustom -> {
                    modifiedMapper.partialUpdate(existingUserCustom, metadata);
                    return existingUserCustom;
                })
                .map(repository::save)
                .map(mapper::toDto);
    }
}
