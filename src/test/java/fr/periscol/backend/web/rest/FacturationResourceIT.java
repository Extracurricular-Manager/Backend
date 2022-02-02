package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Facturation;
import fr.periscol.backend.repository.FacturationRepository;
import fr.periscol.backend.service.dto.FacturationDTO;
import fr.periscol.backend.service.mapper.FacturationMapper;
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
 * Integration tests for the {@link FacturationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturationResourceIT {

    private static final String DEFAULT_SCHOOL_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL_SERVICE = "BBBBBBBBBB";

    private static final Long DEFAULT_COST = 1L;
    private static final Long UPDATED_COST = 2L;

    private static final Boolean DEFAULT_PAYED = false;
    private static final Boolean UPDATED_PAYED = true;

    private static final String ENTITY_API_URL = "/api/facturations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturationRepository facturationRepository;

    @Autowired
    private FacturationMapper facturationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturationMockMvc;

    private Facturation facturation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturation createEntity(EntityManager em) {
        Facturation facturation = new Facturation().schoolService(DEFAULT_SCHOOL_SERVICE).cost(DEFAULT_COST).payed(DEFAULT_PAYED);
        return facturation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facturation createUpdatedEntity(EntityManager em) {
        Facturation facturation = new Facturation().schoolService(UPDATED_SCHOOL_SERVICE).cost(UPDATED_COST).payed(UPDATED_PAYED);
        return facturation;
    }

    @BeforeEach
    public void initTest() {
        facturation = createEntity(em);
    }

    @Test
    @Transactional
    void createFacturation() throws Exception {
        int databaseSizeBeforeCreate = facturationRepository.findAll().size();
        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);
        restFacturationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeCreate + 1);
        Facturation testFacturation = facturationList.get(facturationList.size() - 1);
        assertThat(testFacturation.getSchoolService()).isEqualTo(DEFAULT_SCHOOL_SERVICE);
        assertThat(testFacturation.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testFacturation.getPayed()).isEqualTo(DEFAULT_PAYED);
    }

    @Test
    @Transactional
    void createFacturationWithExistingId() throws Exception {
        // Create the Facturation with an existing ID
        facturation.setId(1L);
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        int databaseSizeBeforeCreate = facturationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturations() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        // Get all the facturationList
        restFacturationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facturation.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolService").value(hasItem(DEFAULT_SCHOOL_SERVICE)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].payed").value(hasItem(DEFAULT_PAYED.booleanValue())));
    }

    @Test
    @Transactional
    void getFacturation() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        // Get the facturation
        restFacturationMockMvc
            .perform(get(ENTITY_API_URL_ID, facturation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facturation.getId().intValue()))
            .andExpect(jsonPath("$.schoolService").value(DEFAULT_SCHOOL_SERVICE))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.payed").value(DEFAULT_PAYED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFacturation() throws Exception {
        // Get the facturation
        restFacturationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacturation() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();

        // Update the facturation
        Facturation updatedFacturation = facturationRepository.findById(facturation.getId()).get();
        // Disconnect from session so that the updates on updatedFacturation are not directly saved in db
        em.detach(updatedFacturation);
        updatedFacturation.schoolService(UPDATED_SCHOOL_SERVICE).cost(UPDATED_COST).payed(UPDATED_PAYED);
        FacturationDTO facturationDTO = facturationMapper.toDto(updatedFacturation);

        restFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
        Facturation testFacturation = facturationList.get(facturationList.size() - 1);
        assertThat(testFacturation.getSchoolService()).isEqualTo(UPDATED_SCHOOL_SERVICE);
        assertThat(testFacturation.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testFacturation.getPayed()).isEqualTo(UPDATED_PAYED);
    }

    @Test
    @Transactional
    void putNonExistingFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturationWithPatch() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();

        // Update the facturation using partial update
        Facturation partialUpdatedFacturation = new Facturation();
        partialUpdatedFacturation.setId(facturation.getId());

        partialUpdatedFacturation.cost(UPDATED_COST).payed(UPDATED_PAYED);

        restFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturation))
            )
            .andExpect(status().isOk());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
        Facturation testFacturation = facturationList.get(facturationList.size() - 1);
        assertThat(testFacturation.getSchoolService()).isEqualTo(DEFAULT_SCHOOL_SERVICE);
        assertThat(testFacturation.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testFacturation.getPayed()).isEqualTo(UPDATED_PAYED);
    }

    @Test
    @Transactional
    void fullUpdateFacturationWithPatch() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();

        // Update the facturation using partial update
        Facturation partialUpdatedFacturation = new Facturation();
        partialUpdatedFacturation.setId(facturation.getId());

        partialUpdatedFacturation.schoolService(UPDATED_SCHOOL_SERVICE).cost(UPDATED_COST).payed(UPDATED_PAYED);

        restFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacturation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacturation))
            )
            .andExpect(status().isOk());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
        Facturation testFacturation = facturationList.get(facturationList.size() - 1);
        assertThat(testFacturation.getSchoolService()).isEqualTo(UPDATED_SCHOOL_SERVICE);
        assertThat(testFacturation.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testFacturation.getPayed()).isEqualTo(UPDATED_PAYED);
    }

    @Test
    @Transactional
    void patchNonExistingFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacturation() throws Exception {
        int databaseSizeBeforeUpdate = facturationRepository.findAll().size();
        facturation.setId(count.incrementAndGet());

        // Create the Facturation
        FacturationDTO facturationDTO = facturationMapper.toDto(facturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facturation in the database
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacturation() throws Exception {
        // Initialize the database
        facturationRepository.saveAndFlush(facturation);

        int databaseSizeBeforeDelete = facturationRepository.findAll().size();

        // Delete the facturation
        restFacturationMockMvc
            .perform(delete(ENTITY_API_URL_ID, facturation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facturation> facturationList = facturationRepository.findAll();
        assertThat(facturationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
