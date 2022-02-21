package fr.periscol.backend;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.dto.MetaDataBackendDTO;
import fr.periscol.backend.service.mapper.MetaDataBackendMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MetaDataBackendInit {

    private final MetaDataBackendRepository metaDataBackendRepository;
    private final MetaDataBackendMapper metaDataBackendMapper;

    @Value("${periscol.api-version}")
    private String versionInProperties;

    public MetaDataBackendInit(MetaDataBackendRepository metaDataBackendRepository, MetaDataBackendMapper metaDataBackendMapper) {
        this.metaDataBackendRepository = metaDataBackendRepository;
        this.metaDataBackendMapper = metaDataBackendMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initMetaDataBackend() {
        if (metaDataBackendRepository.findAll().isEmpty()) {
            MetaDataBackend m = this.metaDataBackendMapper.toEntity(createBeanMetaDataBackendInit());
            metaDataBackendRepository.saveAndFlush(m);
        } else if (metaDataBackendRepository.findAll().get(0).getVersion().equals(versionInProperties)) {
            metaDataBackendRepository.saveAndFlush(createBeanMetaDataBackendFromDatabase());
        }
    }

    private MetaDataBackendDTO createBeanMetaDataBackendInit() {
        MetaDataBackendDTO metaDataBackend = new MetaDataBackendDTO();
        //initialisation with empty String for school
        metaDataBackend.setNameOfSchool("");
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }

    private MetaDataBackend createBeanMetaDataBackendFromDatabase() {
        MetaDataBackend metaDataBackend = metaDataBackendRepository.findAll().get(0);
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }
}
