package fr.periscol.backend;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.MetaDataBackendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MetaDataBackendInit {

    private final MetaDataBackendRepository metaDataBackendRepository;

    @Value("${periscol.api-version}")
    private String versionInProperties;

    public MetaDataBackendInit(MetaDataBackendRepository metaDataBackendRepository) {
        this.metaDataBackendRepository = metaDataBackendRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initMetaDataBackend(){
        if (metaDataBackendRepository.findAll().isEmpty()){
            metaDataBackendRepository.saveAndFlush(createBeanMetaDataBackendInit());
        }
        else if (metaDataBackendRepository.findAll().get(0).getVersion().equals(versionInProperties)){
            metaDataBackendRepository.saveAndFlush(createBeanMetaDataBackendFromDatabase());
        }
    }

    private MetaDataBackend createBeanMetaDataBackendInit(){
        MetaDataBackend metaDataBackend = new MetaDataBackend();
        //initialisation with empty String for id
        metaDataBackend.setNameOfSchool("");
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }

    private MetaDataBackend createBeanMetaDataBackendFromDatabase(){
        MetaDataBackend metaDataBackend = metaDataBackendRepository.findAll().get(0);
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }
}
