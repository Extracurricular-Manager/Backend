package fr.periscol.backend.web.rest;

import fr.periscol.backend.IntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.repository.MetaDataBackendRepository;
import fr.periscol.backend.service.MetaDataBackendService;
import fr.periscol.backend.service.mapper.MetaDataBackendMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MetaDataBackendResourceIT {

    private static final String ENTITY_API_URL = "/api/info";

    @Autowired
    private MetaDataBackendRepository metaDataBackendRepository;

    @Mock
    private MetaDataBackendRepository metaDataBackendRepositoryMock;

    @Autowired
    private MetaDataBackendMapper metaDataBackendMapper;

    @Mock
    private MetaDataBackendService metaDataBackendServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaDataBackendMockMvc;

    @Test
    @Transactional
    public void getMetaDataBackend() throws Exception {
        MetaDataBackend metaDataBackend = metaDataBackendRepository.findAll().get(0);
        restMetaDataBackendMockMvc.perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.nameOfSchool").value(metaDataBackend.getNameOfSchool()))
                .andExpect(jsonPath("$.version").value(metaDataBackend.getVersion()));
    }

    @Test
    @Transactional
    public void patchMetaDataBackendName() throws Exception {
        MetaDataBackend metaDataBackend = metaDataBackendRepository.findAll().get(0);
        String name = "periscolSchool";
        restMetaDataBackendMockMvc.perform(patch(ENTITY_API_URL).content(name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.nameOfSchool").value(name))
                .andExpect(jsonPath("$.version").value(metaDataBackend.getVersion()));
    }
}
