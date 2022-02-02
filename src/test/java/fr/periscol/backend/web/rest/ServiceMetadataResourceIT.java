package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.ServiceMetadata;
import fr.periscol.backend.repository.ServiceMetadataRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceMetadataResourceIT {

    private static final String DEFAULT_MODULE = "AAAAAAAAAA";
    private static final String UPDATED_MODULE = "BBBBBBBBBB";

    private static final String DEFAULT_END_POINT = "AAAAAAAAAA";
    private static final String UPDATED_END_POINT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceMetadataRepository serviceMetadataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceMetadataMockMvc;

    private ServiceMetadata serviceMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMetadata createEntity(EntityManager em) {
        ServiceMetadata serviceMetadata = new ServiceMetadata().module(DEFAULT_MODULE).endPoint(DEFAULT_END_POINT);
        return serviceMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceMetadata createUpdatedEntity(EntityManager em) {
        ServiceMetadata serviceMetadata = new ServiceMetadata().module(UPDATED_MODULE).endPoint(UPDATED_END_POINT);
        return serviceMetadata;
    }

    @BeforeEach
    public void initTest() {
        serviceMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceMetadata() throws Exception {
        int databaseSizeBeforeCreate = serviceMetadataRepository.findAll().size();
        // Create the ServiceMetadata
        restServiceMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceMetadata testServiceMetadata = serviceMetadataList.get(serviceMetadataList.size() - 1);
        assertThat(testServiceMetadata.getModule()).isEqualTo(DEFAULT_MODULE);
        assertThat(testServiceMetadata.getEndPoint()).isEqualTo(DEFAULT_END_POINT);
    }

    @Test
    @Transactional
    void createServiceMetadataWithExistingId() throws Exception {
        // Create the ServiceMetadata with an existing ID
        serviceMetadata.setId(1L);

        int databaseSizeBeforeCreate = serviceMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceMetadata() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        // Get all the serviceMetadataList
        restServiceMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE)))
            .andExpect(jsonPath("$.[*].endPoint").value(hasItem(DEFAULT_END_POINT)));
    }

    @Test
    @Transactional
    void getServiceMetadata() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        // Get the serviceMetadata
        restServiceMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceMetadata.getId().intValue()))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE))
            .andExpect(jsonPath("$.endPoint").value(DEFAULT_END_POINT));
    }

    @Test
    @Transactional
    void getNonExistingServiceMetadata() throws Exception {
        // Get the serviceMetadata
        restServiceMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceMetadata() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();

        // Update the serviceMetadata
        ServiceMetadata updatedServiceMetadata = serviceMetadataRepository.findById(serviceMetadata.getId()).get();
        // Disconnect from session so that the updates on updatedServiceMetadata are not directly saved in db
        em.detach(updatedServiceMetadata);
        updatedServiceMetadata.module(UPDATED_MODULE).endPoint(UPDATED_END_POINT);

        restServiceMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceMetadata.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
        ServiceMetadata testServiceMetadata = serviceMetadataList.get(serviceMetadataList.size() - 1);
        assertThat(testServiceMetadata.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testServiceMetadata.getEndPoint()).isEqualTo(UPDATED_END_POINT);
    }

    @Test
    @Transactional
    void putNonExistingServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceMetadata.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceMetadataWithPatch() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();

        // Update the serviceMetadata using partial update
        ServiceMetadata partialUpdatedServiceMetadata = new ServiceMetadata();
        partialUpdatedServiceMetadata.setId(serviceMetadata.getId());

        restServiceMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
        ServiceMetadata testServiceMetadata = serviceMetadataList.get(serviceMetadataList.size() - 1);
        assertThat(testServiceMetadata.getModule()).isEqualTo(DEFAULT_MODULE);
        assertThat(testServiceMetadata.getEndPoint()).isEqualTo(DEFAULT_END_POINT);
    }

    @Test
    @Transactional
    void fullUpdateServiceMetadataWithPatch() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();

        // Update the serviceMetadata using partial update
        ServiceMetadata partialUpdatedServiceMetadata = new ServiceMetadata();
        partialUpdatedServiceMetadata.setId(serviceMetadata.getId());

        partialUpdatedServiceMetadata.module(UPDATED_MODULE).endPoint(UPDATED_END_POINT);

        restServiceMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
        ServiceMetadata testServiceMetadata = serviceMetadataList.get(serviceMetadataList.size() - 1);
        assertThat(testServiceMetadata.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testServiceMetadata.getEndPoint()).isEqualTo(UPDATED_END_POINT);
    }

    @Test
    @Transactional
    void patchNonExistingServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceMetadata() throws Exception {
        int databaseSizeBeforeUpdate = serviceMetadataRepository.findAll().size();
        serviceMetadata.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceMetadata))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceMetadata in the database
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceMetadata() throws Exception {
        // Initialize the database
        serviceMetadataRepository.saveAndFlush(serviceMetadata);

        int databaseSizeBeforeDelete = serviceMetadataRepository.findAll().size();

        // Delete the serviceMetadata
        restServiceMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceMetadata> serviceMetadataList = serviceMetadataRepository.findAll();
        assertThat(serviceMetadataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
