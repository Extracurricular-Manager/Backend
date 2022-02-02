package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Modifier;
import fr.periscol.backend.repository.ModifierRepository;
import fr.periscol.backend.service.dto.ModifierDTO;
import fr.periscol.backend.service.mapper.ModifierMapper;
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
 * Integration tests for the {@link ModifierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ModifierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/modifiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ModifierRepository modifierRepository;

    @Autowired
    private ModifierMapper modifierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModifierMockMvc;

    private Modifier modifier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modifier createEntity(EntityManager em) {
        Modifier modifier = new Modifier().name(DEFAULT_NAME);
        return modifier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modifier createUpdatedEntity(EntityManager em) {
        Modifier modifier = new Modifier().name(UPDATED_NAME);
        return modifier;
    }

    @BeforeEach
    public void initTest() {
        modifier = createEntity(em);
    }

    @Test
    @Transactional
    void createModifier() throws Exception {
        int databaseSizeBeforeCreate = modifierRepository.findAll().size();
        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);
        restModifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modifierDTO)))
            .andExpect(status().isCreated());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeCreate + 1);
        Modifier testModifier = modifierList.get(modifierList.size() - 1);
        assertThat(testModifier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createModifierWithExistingId() throws Exception {
        // Create the Modifier with an existing ID
        modifier.setId(1L);
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        int databaseSizeBeforeCreate = modifierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModifierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modifierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllModifiers() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        // Get all the modifierList
        restModifierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modifier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getModifier() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        // Get the modifier
        restModifierMockMvc
            .perform(get(ENTITY_API_URL_ID, modifier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(modifier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingModifier() throws Exception {
        // Get the modifier
        restModifierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewModifier() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();

        // Update the modifier
        Modifier updatedModifier = modifierRepository.findById(modifier.getId()).get();
        // Disconnect from session so that the updates on updatedModifier are not directly saved in db
        em.detach(updatedModifier);
        updatedModifier.name(UPDATED_NAME);
        ModifierDTO modifierDTO = modifierMapper.toDto(updatedModifier);

        restModifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modifierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
        Modifier testModifier = modifierList.get(modifierList.size() - 1);
        assertThat(testModifier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modifierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(modifierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModifierWithPatch() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();

        // Update the modifier using partial update
        Modifier partialUpdatedModifier = new Modifier();
        partialUpdatedModifier.setId(modifier.getId());

        restModifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModifier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModifier))
            )
            .andExpect(status().isOk());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
        Modifier testModifier = modifierList.get(modifierList.size() - 1);
        assertThat(testModifier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateModifierWithPatch() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();

        // Update the modifier using partial update
        Modifier partialUpdatedModifier = new Modifier();
        partialUpdatedModifier.setId(modifier.getId());

        partialUpdatedModifier.name(UPDATED_NAME);

        restModifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModifier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedModifier))
            )
            .andExpect(status().isOk());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
        Modifier testModifier = modifierList.get(modifierList.size() - 1);
        assertThat(testModifier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modifierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModifier() throws Exception {
        int databaseSizeBeforeUpdate = modifierRepository.findAll().size();
        modifier.setId(count.incrementAndGet());

        // Create the Modifier
        ModifierDTO modifierDTO = modifierMapper.toDto(modifier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModifierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(modifierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Modifier in the database
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModifier() throws Exception {
        // Initialize the database
        modifierRepository.saveAndFlush(modifier);

        int databaseSizeBeforeDelete = modifierRepository.findAll().size();

        // Delete the modifier
        restModifierMockMvc
            .perform(delete(ENTITY_API_URL_ID, modifier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Modifier> modifierList = modifierRepository.findAll();
        assertThat(modifierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
