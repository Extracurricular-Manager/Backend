package fr.periscol.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.repository.PermissionRepository;
import fr.periscol.backend.repository.service_model.ServiceMetadataRepository;
import fr.periscol.backend.service.dto.service_model.ModifiedServiceMetadataDTO;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles={"ADMIN"})
class MetadataResourceIT {

    private static final String ENTITY_API_URL = "/api/service";
    private static final String ENTITY_API_URL_ALL = "/api/services";
    private static final String PERMISSION_API_URL = "/api/permissions";

    @Autowired
    private ServiceMetadataRepository repository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ServiceMetadataService serviceMetadata;

    @Autowired
    private MockMvc serviceMetadataMockMvc;

    @Autowired
    private EntityManager em;

    void addChild(EntityManager em) {
        final var metadata = new NewServiceMetadataDTO();
        metadata.setName("Michel");
        metadata.setModel("presence");
        metadata.setIcon("foo");
        serviceMetadata.createServiceMetadata(metadata);
    }

    @BeforeEach
    void before() {
        addChild(em);
    }

    @Transactional
    @Test
    void postService() throws Exception {
        final var metadata = new NewServiceMetadataDTO();
        metadata.setName("foo42");
        metadata.setIcon("bar");
        metadata.setModel("presence");
        final var json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(post(ENTITY_API_URL)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        serviceMetadataMockMvc.perform(get(PERMISSION_API_URL))
                .andExpect(jsonPath("$.[*].name").value(hasItem(metadata.getName())));

    }

    @Transactional
    @Test
    void postExistingService() throws Exception {
        final var metadata = new NewServiceMetadataDTO();
        metadata.setName("Michel");
        metadata.setIcon("foo");
        metadata.setModel("presence");
        String json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(post(ENTITY_API_URL)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Transactional
    @Test
    void postServiceWithNull() throws Exception {
        final var metadata = new NewServiceMetadataDTO();
        metadata.setName("Michel2");
        metadata.setIcon("foo");
        String json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(post(ENTITY_API_URL)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Transactional
    @Test
    void postNonExistingService() throws Exception {
        final var metadata = new NewServiceMetadataDTO();
        metadata.setName("Michel2");
        metadata.setIcon("foo");
        metadata.setModel("bar");
        String json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(post(ENTITY_API_URL)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());

    }

    @Transactional
    @Test
    void deleteExistingService() throws Exception {
        final var serviceOpt = repository.findOneByName("Michel");
        assertFalse(serviceOpt.isEmpty());

        serviceMetadataMockMvc.perform(get(PERMISSION_API_URL))
                .andExpect(jsonPath("$.[*].name").value(hasItem(serviceOpt.get().getName())));

        serviceMetadataMockMvc.perform(delete(ENTITY_API_URL + "/" + serviceOpt.get().getId()))
                .andExpect(status().isNoContent());

        serviceMetadataMockMvc.perform(get(PERMISSION_API_URL))
                .andExpect(jsonPath("$.[*].name").value(Matchers.not(hasItem(serviceOpt.get().getName()))));
    }

    @Transactional
    @Test
    void deleteNonExistingService() throws Exception {
        serviceMetadataMockMvc.perform(delete(ENTITY_API_URL + "/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void getExistingService() throws Exception {
        final var serviceOpt = repository.findOneByName("Michel");
        assertFalse(serviceOpt.isEmpty());
        serviceMetadataMockMvc.perform(get(ENTITY_API_URL + "/" + serviceOpt.get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(serviceOpt.get().getId()));
    }

    @Transactional
    @Test
    void getNonExistingService() throws Exception {
        serviceMetadataMockMvc.perform(get(ENTITY_API_URL + "/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    void getAllServices() throws Exception {
        var metadata = new ServiceMetadata();
        metadata.setName("Michel2");
        metadata.setIcon("foo");
        metadata.setModel("period");
        metadata = repository.saveAndFlush(metadata);
        final var serviceOpt = repository.findOneByName("Michel");
        assertFalse(serviceOpt.isEmpty());
        serviceMetadataMockMvc.perform(get(ENTITY_API_URL_ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().intValue())))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOpt.get().getId().intValue())));
    }

    @Transactional
    @Test
    void patchService() throws Exception {
        final var serviceOpt = repository.findOneByName("Michel");
        assertFalse(serviceOpt.isEmpty());

        serviceMetadataMockMvc.perform(get(PERMISSION_API_URL))
                .andExpect(jsonPath("$.[*].name").value(hasItem(serviceOpt.get().getName())));

        var metadata = new ModifiedServiceMetadataDTO();
        metadata.setName("Michel2");
        metadata.setIcon("foo");

        final var json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(patch(ENTITY_API_URL + "/" + serviceOpt.get().getId())
                        .contentType("application/merge-patch+json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(serviceOpt.get().getId().intValue()))
                .andExpect(jsonPath("$.name").value(metadata.getName()))
                .andExpect(jsonPath("$.icon").value(metadata.getIcon()));

        serviceMetadataMockMvc.perform(get(PERMISSION_API_URL))
                .andExpect(jsonPath("$.[*].name").value(hasItem(metadata.getName())));
    }

    @Transactional
    @Test
    void patchNonExistingService() throws Exception {
        final var metadata = new ModifiedServiceMetadataDTO();
        metadata.setName("Michel2");
        metadata.setIcon("foo");
        final var json = new ObjectMapper().writeValueAsString(metadata);
        serviceMetadataMockMvc.perform(patch(ENTITY_API_URL + "/" + Integer.MAX_VALUE)
                        .contentType("application/merge-patch+json")
                        .content(json))
                .andExpect(status().isNotFound());
        assertFalse(repository.existsById((long) Integer.MAX_VALUE));
    }
}
