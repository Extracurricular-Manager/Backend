package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.UserCustom;
import fr.periscol.backend.repository.UserCustomRepository;
import fr.periscol.backend.service.UserCustomService;
import fr.periscol.backend.service.dto.UserCustomDTO;
import fr.periscol.backend.service.mapper.UserCustomMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserCustomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserCustomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-customs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCustomRepository userCustomRepository;

    @Mock
    private UserCustomRepository userCustomRepositoryMock;

    @Autowired
    private UserCustomMapper userCustomMapper;

    @Mock
    private UserCustomService userCustomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCustomMockMvc;

    private UserCustom userCustom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCustom createEntity(EntityManager em) {
        UserCustom userCustom = new UserCustom().name(DEFAULT_NAME).login(DEFAULT_LOGIN).password(DEFAULT_PASSWORD);
        return userCustom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCustom createUpdatedEntity(EntityManager em) {
        UserCustom userCustom = new UserCustom().name(UPDATED_NAME).login(UPDATED_LOGIN).password(UPDATED_PASSWORD);
        return userCustom;
    }

    @BeforeEach
    public void initTest() {
        userCustom = createEntity(em);
    }

    @Test
    @Transactional
    void createUserCustom() throws Exception {
        int databaseSizeBeforeCreate = userCustomRepository.findAll().size();
        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);
        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustomDTO)))
            .andExpect(status().isCreated());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeCreate + 1);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserCustom.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUserCustom.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createUserCustomWithExistingId() throws Exception {
        // Create the UserCustom with an existing ID
        userCustom.setId(1L);
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        int databaseSizeBeforeCreate = userCustomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserCustoms() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        // Get all the userCustomList
        restUserCustomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCustom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCustomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userCustomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCustomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userCustomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCustomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userCustomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCustomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userCustomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        // Get the userCustom
        restUserCustomMockMvc
            .perform(get(ENTITY_API_URL_ID, userCustom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCustom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingUserCustom() throws Exception {
        // Get the userCustom
        restUserCustomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();

        // Update the userCustom
        UserCustom updatedUserCustom = userCustomRepository.findById(userCustom.getId()).get();
        // Disconnect from session so that the updates on updatedUserCustom are not directly saved in db
        em.detach(updatedUserCustom);
        updatedUserCustom.name(UPDATED_NAME).login(UPDATED_LOGIN).password(UPDATED_PASSWORD);
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(updatedUserCustom);

        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCustom.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUserCustom.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCustomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCustomWithPatch() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();

        // Update the userCustom using partial update
        UserCustom partialUpdatedUserCustom = new UserCustom();
        partialUpdatedUserCustom.setId(userCustom.getId());

        partialUpdatedUserCustom.name(UPDATED_NAME);

        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCustom))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCustom.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUserCustom.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateUserCustomWithPatch() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();

        // Update the userCustom using partial update
        UserCustom partialUpdatedUserCustom = new UserCustom();
        partialUpdatedUserCustom.setId(userCustom.getId());

        partialUpdatedUserCustom.name(UPDATED_NAME).login(UPDATED_LOGIN).password(UPDATED_PASSWORD);

        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCustom))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserCustom.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUserCustom.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userCustomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustom.setId(count.incrementAndGet());

        // Create the UserCustom
        UserCustomDTO userCustomDTO = userCustomMapper.toDto(userCustom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userCustomDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeDelete = userCustomRepository.findAll().size();

        // Delete the userCustom
        restUserCustomMockMvc
            .perform(delete(ENTITY_API_URL_ID, userCustom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
