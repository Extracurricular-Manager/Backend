package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Tarification;
import fr.periscol.backend.repository.TarificationRepository;
import fr.periscol.backend.service.dto.TarificationDTO;
import fr.periscol.backend.service.mapper.TarificationMapper;
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
 * Integration tests for the {@link TarificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarificationResourceIT {

    private static final String ENTITY_API_URL = "/api/tarifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarificationRepository tarificationRepository;

    @Autowired
    private TarificationMapper tarificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarificationMockMvc;

    private Tarification tarification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarification createEntity(EntityManager em) {
        Tarification tarification = new Tarification();
        return tarification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarification createUpdatedEntity(EntityManager em) {
        Tarification tarification = new Tarification();
        return tarification;
    }

    @BeforeEach
    public void initTest() {
        tarification = createEntity(em);
    }

    @Test
    @Transactional
    void createTarification() throws Exception {
        int databaseSizeBeforeCreate = tarificationRepository.findAll().size();
        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);
        restTarificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeCreate + 1);
        Tarification testTarification = tarificationList.get(tarificationList.size() - 1);
    }

    @Test
    @Transactional
    void createTarificationWithExistingId() throws Exception {
        // Create the Tarification with an existing ID
        tarification.setId(1L);
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        int databaseSizeBeforeCreate = tarificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTarifications() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        // Get all the tarificationList
        restTarificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarification.getId().intValue())));
    }

    @Test
    @Transactional
    void getTarification() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        // Get the tarification
        restTarificationMockMvc
            .perform(get(ENTITY_API_URL_ID, tarification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarification.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTarification() throws Exception {
        // Get the tarification
        restTarificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTarification() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();

        // Update the tarification
        Tarification updatedTarification = tarificationRepository.findById(tarification.getId()).get();
        // Disconnect from session so that the updates on updatedTarification are not directly saved in db
        em.detach(updatedTarification);
        TarificationDTO tarificationDTO = tarificationMapper.toDto(updatedTarification);

        restTarificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
        Tarification testTarification = tarificationList.get(tarificationList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarificationWithPatch() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();

        // Update the tarification using partial update
        Tarification partialUpdatedTarification = new Tarification();
        partialUpdatedTarification.setId(tarification.getId());

        restTarificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarification))
            )
            .andExpect(status().isOk());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
        Tarification testTarification = tarificationList.get(tarificationList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTarificationWithPatch() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();

        // Update the tarification using partial update
        Tarification partialUpdatedTarification = new Tarification();
        partialUpdatedTarification.setId(tarification.getId());

        restTarificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarification))
            )
            .andExpect(status().isOk());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
        Tarification testTarification = tarificationList.get(tarificationList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarification() throws Exception {
        int databaseSizeBeforeUpdate = tarificationRepository.findAll().size();
        tarification.setId(count.incrementAndGet());

        // Create the Tarification
        TarificationDTO tarificationDTO = tarificationMapper.toDto(tarification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarification in the database
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarification() throws Exception {
        // Initialize the database
        tarificationRepository.saveAndFlush(tarification);

        int databaseSizeBeforeDelete = tarificationRepository.findAll().size();

        // Delete the tarification
        restTarificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tarification> tarificationList = tarificationRepository.findAll();
        assertThat(tarificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
