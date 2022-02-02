package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.TarifBase;
import fr.periscol.backend.repository.TarifBaseRepository;
import fr.periscol.backend.service.dto.TarifBaseDTO;
import fr.periscol.backend.service.mapper.TarifBaseMapper;
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
 * Integration tests for the {@link TarifBaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarifBaseResourceIT {

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tarif-bases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarifBaseRepository tarifBaseRepository;

    @Autowired
    private TarifBaseMapper tarifBaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifBaseMockMvc;

    private TarifBase tarifBase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TarifBase createEntity(EntityManager em) {
        TarifBase tarifBase = new TarifBase().price(DEFAULT_PRICE).name(DEFAULT_NAME).comment(DEFAULT_COMMENT);
        return tarifBase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TarifBase createUpdatedEntity(EntityManager em) {
        TarifBase tarifBase = new TarifBase().price(UPDATED_PRICE).name(UPDATED_NAME).comment(UPDATED_COMMENT);
        return tarifBase;
    }

    @BeforeEach
    public void initTest() {
        tarifBase = createEntity(em);
    }

    @Test
    @Transactional
    void createTarifBase() throws Exception {
        int databaseSizeBeforeCreate = tarifBaseRepository.findAll().size();
        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);
        restTarifBaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO)))
            .andExpect(status().isCreated());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeCreate + 1);
        TarifBase testTarifBase = tarifBaseList.get(tarifBaseList.size() - 1);
        assertThat(testTarifBase.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTarifBase.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTarifBase.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createTarifBaseWithExistingId() throws Exception {
        // Create the TarifBase with an existing ID
        tarifBase.setId(1L);
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        int databaseSizeBeforeCreate = tarifBaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifBaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTarifBases() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        // Get all the tarifBaseList
        restTarifBaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarifBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getTarifBase() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        // Get the tarifBase
        restTarifBaseMockMvc
            .perform(get(ENTITY_API_URL_ID, tarifBase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarifBase.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingTarifBase() throws Exception {
        // Get the tarifBase
        restTarifBaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTarifBase() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();

        // Update the tarifBase
        TarifBase updatedTarifBase = tarifBaseRepository.findById(tarifBase.getId()).get();
        // Disconnect from session so that the updates on updatedTarifBase are not directly saved in db
        em.detach(updatedTarifBase);
        updatedTarifBase.price(UPDATED_PRICE).name(UPDATED_NAME).comment(UPDATED_COMMENT);
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(updatedTarifBase);

        restTarifBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifBaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
        TarifBase testTarifBase = tarifBaseList.get(tarifBaseList.size() - 1);
        assertThat(testTarifBase.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTarifBase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTarifBase.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifBaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifBaseWithPatch() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();

        // Update the tarifBase using partial update
        TarifBase partialUpdatedTarifBase = new TarifBase();
        partialUpdatedTarifBase.setId(tarifBase.getId());

        partialUpdatedTarifBase.price(UPDATED_PRICE).name(UPDATED_NAME).comment(UPDATED_COMMENT);

        restTarifBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarifBase))
            )
            .andExpect(status().isOk());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
        TarifBase testTarifBase = tarifBaseList.get(tarifBaseList.size() - 1);
        assertThat(testTarifBase.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTarifBase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTarifBase.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateTarifBaseWithPatch() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();

        // Update the tarifBase using partial update
        TarifBase partialUpdatedTarifBase = new TarifBase();
        partialUpdatedTarifBase.setId(tarifBase.getId());

        partialUpdatedTarifBase.price(UPDATED_PRICE).name(UPDATED_NAME).comment(UPDATED_COMMENT);

        restTarifBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarifBase))
            )
            .andExpect(status().isOk());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
        TarifBase testTarifBase = tarifBaseList.get(tarifBaseList.size() - 1);
        assertThat(testTarifBase.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTarifBase.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTarifBase.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarifBaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarifBase() throws Exception {
        int databaseSizeBeforeUpdate = tarifBaseRepository.findAll().size();
        tarifBase.setId(count.incrementAndGet());

        // Create the TarifBase
        TarifBaseDTO tarifBaseDTO = tarifBaseMapper.toDto(tarifBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifBaseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tarifBaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TarifBase in the database
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarifBase() throws Exception {
        // Initialize the database
        tarifBaseRepository.saveAndFlush(tarifBase);

        int databaseSizeBeforeDelete = tarifBaseRepository.findAll().size();

        // Delete the tarifBase
        restTarifBaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarifBase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TarifBase> tarifBaseList = tarifBaseRepository.findAll();
        assertThat(tarifBaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
