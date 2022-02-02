package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.PresenceModel;
import fr.periscol.backend.repository.PresenceModelRepository;
import fr.periscol.backend.service.dto.PresenceModelDTO;
import fr.periscol.backend.service.mapper.PresenceModelMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PresenceModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresenceModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRESENCE = false;
    private static final Boolean UPDATED_PRESENCE = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/presence-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresenceModelRepository presenceModelRepository;

    @Autowired
    private PresenceModelMapper presenceModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresenceModelMockMvc;

    private PresenceModel presenceModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PresenceModel createEntity(EntityManager em) {
        PresenceModel presenceModel = new PresenceModel().name(DEFAULT_NAME).presence(DEFAULT_PRESENCE).date(DEFAULT_DATE);
        return presenceModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PresenceModel createUpdatedEntity(EntityManager em) {
        PresenceModel presenceModel = new PresenceModel().name(UPDATED_NAME).presence(UPDATED_PRESENCE).date(UPDATED_DATE);
        return presenceModel;
    }

    @BeforeEach
    public void initTest() {
        presenceModel = createEntity(em);
    }

    @Test
    @Transactional
    void createPresenceModel() throws Exception {
        int databaseSizeBeforeCreate = presenceModelRepository.findAll().size();
        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);
        restPresenceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeCreate + 1);
        PresenceModel testPresenceModel = presenceModelList.get(presenceModelList.size() - 1);
        assertThat(testPresenceModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPresenceModel.getPresence()).isEqualTo(DEFAULT_PRESENCE);
        assertThat(testPresenceModel.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPresenceModelWithExistingId() throws Exception {
        // Create the PresenceModel with an existing ID
        presenceModel.setId(1L);
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        int databaseSizeBeforeCreate = presenceModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresenceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPresenceModels() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        // Get all the presenceModelList
        restPresenceModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presenceModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].presence").value(hasItem(DEFAULT_PRESENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPresenceModel() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        // Get the presenceModel
        restPresenceModelMockMvc
            .perform(get(ENTITY_API_URL_ID, presenceModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presenceModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.presence").value(DEFAULT_PRESENCE.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPresenceModel() throws Exception {
        // Get the presenceModel
        restPresenceModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPresenceModel() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();

        // Update the presenceModel
        PresenceModel updatedPresenceModel = presenceModelRepository.findById(presenceModel.getId()).get();
        // Disconnect from session so that the updates on updatedPresenceModel are not directly saved in db
        em.detach(updatedPresenceModel);
        updatedPresenceModel.name(UPDATED_NAME).presence(UPDATED_PRESENCE).date(UPDATED_DATE);
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(updatedPresenceModel);

        restPresenceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presenceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
        PresenceModel testPresenceModel = presenceModelList.get(presenceModelList.size() - 1);
        assertThat(testPresenceModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPresenceModel.getPresence()).isEqualTo(UPDATED_PRESENCE);
        assertThat(testPresenceModel.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presenceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresenceModelWithPatch() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();

        // Update the presenceModel using partial update
        PresenceModel partialUpdatedPresenceModel = new PresenceModel();
        partialUpdatedPresenceModel.setId(presenceModel.getId());

        partialUpdatedPresenceModel.date(UPDATED_DATE);

        restPresenceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresenceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresenceModel))
            )
            .andExpect(status().isOk());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
        PresenceModel testPresenceModel = presenceModelList.get(presenceModelList.size() - 1);
        assertThat(testPresenceModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPresenceModel.getPresence()).isEqualTo(DEFAULT_PRESENCE);
        assertThat(testPresenceModel.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePresenceModelWithPatch() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();

        // Update the presenceModel using partial update
        PresenceModel partialUpdatedPresenceModel = new PresenceModel();
        partialUpdatedPresenceModel.setId(presenceModel.getId());

        partialUpdatedPresenceModel.name(UPDATED_NAME).presence(UPDATED_PRESENCE).date(UPDATED_DATE);

        restPresenceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresenceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresenceModel))
            )
            .andExpect(status().isOk());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
        PresenceModel testPresenceModel = presenceModelList.get(presenceModelList.size() - 1);
        assertThat(testPresenceModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPresenceModel.getPresence()).isEqualTo(UPDATED_PRESENCE);
        assertThat(testPresenceModel.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presenceModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresenceModel() throws Exception {
        int databaseSizeBeforeUpdate = presenceModelRepository.findAll().size();
        presenceModel.setId(count.incrementAndGet());

        // Create the PresenceModel
        PresenceModelDTO presenceModelDTO = presenceModelMapper.toDto(presenceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresenceModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presenceModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PresenceModel in the database
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresenceModel() throws Exception {
        // Initialize the database
        presenceModelRepository.saveAndFlush(presenceModel);

        int databaseSizeBeforeDelete = presenceModelRepository.findAll().size();

        // Delete the presenceModel
        restPresenceModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, presenceModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PresenceModel> presenceModelList = presenceModelRepository.findAll();
        assertThat(presenceModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
