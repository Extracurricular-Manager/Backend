package fr.periscol.backend.service;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.dto.MetaDataBackendDTO;
import fr.periscol.backend.service.mapper.MetaDataBackendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MetaDataBackendService {

    private final MetaDataBackendMapper metaDataBackendMapper;
    private final MetaDataBackendRepository metaDataBackendRepository;

    private final Logger log = LoggerFactory.getLogger(MetaDataBackendService.class);

    public MetaDataBackendService(MetaDataBackendMapper metaDataBackendMapper, MetaDataBackendRepository metaDataBackendRepository) {
        this.metaDataBackendMapper = metaDataBackendMapper;
        this.metaDataBackendRepository = metaDataBackendRepository;
    }

    /**
     * Save a metaDataBackend.
     *
     * @param metaDataBackendDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaDataBackendDTO save(MetaDataBackendDTO metaDataBackendDTO) {
        log.debug("Request to save MetaDataBackend : {}", metaDataBackendDTO);
        MetaDataBackend metaDataBackend = metaDataBackendMapper.toEntity(metaDataBackendDTO);
        metaDataBackend = metaDataBackendRepository.save(metaDataBackend);
        return metaDataBackendMapper.toDto(metaDataBackend);
    }

    /**
     * Return the MetaData of the Backend.
     *
     * @return the persisted entity.
     */
    @Transactional(readOnly = true)
    public MetaDataBackendDTO findIt(){
        log.debug("Request to get the MetaDataBackend");
        return metaDataBackendMapper.toDto(metaDataBackendRepository.findAll().get(0));
    }

    /**
     * Update the name of the school of the metaData of the backend.
     *
     * @param name The new name of the school of the metaData.
     * @return The updated entity.
     */
    public MetaDataBackendDTO updateName(String name){
        log.debug("Request to update the name of the school in the metaDataBackend");
        MetaDataBackendDTO metaDataBackendDTO = findIt();
        metaDataBackendRepository.delete(metaDataBackendMapper.toEntity(metaDataBackendDTO));
        metaDataBackendDTO.setNameOfSchool(name);
        metaDataBackendRepository.save(metaDataBackendMapper.toEntity(metaDataBackendDTO));
        return metaDataBackendDTO;
    }
}
