package fr.periscol.backend;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.dto.MetaDataBackendDTO;
import fr.periscol.backend.service.mapper.MetaDataBackendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class MetaDataBackendInit {

    private final MetaDataBackendRepository metaDataBackendRepository;
    private final MetaDataBackendMapper metaDataBackendMapper;
    private final Logger log = LoggerFactory.getLogger(MetaDataBackendInit.class);

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
        metaDataBackend.setNameOfSchool(getRandomName("default_backend_names.txt"));
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }

    private MetaDataBackend createBeanMetaDataBackendFromDatabase() {
        MetaDataBackend metaDataBackend = metaDataBackendRepository.findAll().get(0);
        metaDataBackend.setVersion(versionInProperties);
        return metaDataBackend;
    }

    /**
     * @param filePath the path to the resource file where take the name
     * @return A random name from the specified file. Empty string if the file does not exist.
     */
    private String getRandomName(String filePath) {
        final var stream = MetaDataBackendInit.class.getClassLoader().getResourceAsStream(filePath);

        if(stream == null) {
            log.warn("File '{}' not found. Fallback on '\"\"' for backend default name.", filePath);
            return "";
        }

        final var list = new BufferedReader(new InputStreamReader(stream)).lines()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(list);

        return list.get(0);

    }
}
