package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.RoleRole;
import fr.periscol.backend.repository.RoleRoleRepository;
import fr.periscol.backend.service.dto.RoleRoleDTO;
import fr.periscol.backend.service.mapper.RoleRoleMapper;
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
 * Integration tests for the {@link RoleRoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleRoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/role-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleRoleRepository roleRoleRepository;

    @Autowired
    private RoleRoleMapper roleRoleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleRoleMockMvc;

    private RoleRole roleRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleRole createEntity(EntityManager em) {
        RoleRole roleRole = new RoleRole().name(DEFAULT_NAME).permissions(DEFAULT_PERMISSIONS);
        return roleRole;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleRole createUpdatedEntity(EntityManager em) {
        RoleRole roleRole = new RoleRole().name(UPDATED_NAME).permissions(UPDATED_PERMISSIONS);
        return roleRole;
    }

    @BeforeEach
    public void initTest() {
        roleRole = createEntity(em);
    }

    @Test
    @Transactional
    void createRoleRole() throws Exception {
        int databaseSizeBeforeCreate = roleRoleRepository.findAll().size();
        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);
        restRoleRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleRoleDTO)))
            .andExpect(status().isCreated());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeCreate + 1);
        RoleRole testRoleRole = roleRoleList.get(roleRoleList.size() - 1);
        assertThat(testRoleRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoleRole.getPermissions()).isEqualTo(DEFAULT_PERMISSIONS);
    }

    @Test
    @Transactional
    void createRoleRoleWithExistingId() throws Exception {
        // Create the RoleRole with an existing ID
        roleRole.setId(1L);
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        int databaseSizeBeforeCreate = roleRoleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoleRoles() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        // Get all the roleRoleList
        restRoleRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)));
    }

    @Test
    @Transactional
    void getRoleRole() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        // Get the roleRole
        restRoleRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, roleRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.permissions").value(DEFAULT_PERMISSIONS));
    }

    @Test
    @Transactional
    void getNonExistingRoleRole() throws Exception {
        // Get the roleRole
        restRoleRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoleRole() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();

        // Update the roleRole
        RoleRole updatedRoleRole = roleRoleRepository.findById(roleRole.getId()).get();
        // Disconnect from session so that the updates on updatedRoleRole are not directly saved in db
        em.detach(updatedRoleRole);
        updatedRoleRole.name(UPDATED_NAME).permissions(UPDATED_PERMISSIONS);
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(updatedRoleRole);

        restRoleRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
        RoleRole testRoleRole = roleRoleList.get(roleRoleList.size() - 1);
        assertThat(testRoleRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoleRole.getPermissions()).isEqualTo(UPDATED_PERMISSIONS);
    }

    @Test
    @Transactional
    void putNonExistingRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roleRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleRoleWithPatch() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();

        // Update the roleRole using partial update
        RoleRole partialUpdatedRoleRole = new RoleRole();
        partialUpdatedRoleRole.setId(roleRole.getId());

        partialUpdatedRoleRole.name(UPDATED_NAME).permissions(UPDATED_PERMISSIONS);

        restRoleRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleRole))
            )
            .andExpect(status().isOk());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
        RoleRole testRoleRole = roleRoleList.get(roleRoleList.size() - 1);
        assertThat(testRoleRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoleRole.getPermissions()).isEqualTo(UPDATED_PERMISSIONS);
    }

    @Test
    @Transactional
    void fullUpdateRoleRoleWithPatch() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();

        // Update the roleRole using partial update
        RoleRole partialUpdatedRoleRole = new RoleRole();
        partialUpdatedRoleRole.setId(roleRole.getId());

        partialUpdatedRoleRole.name(UPDATED_NAME).permissions(UPDATED_PERMISSIONS);

        restRoleRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleRole))
            )
            .andExpect(status().isOk());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
        RoleRole testRoleRole = roleRoleList.get(roleRoleList.size() - 1);
        assertThat(testRoleRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoleRole.getPermissions()).isEqualTo(UPDATED_PERMISSIONS);
    }

    @Test
    @Transactional
    void patchNonExistingRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleRole() throws Exception {
        int databaseSizeBeforeUpdate = roleRoleRepository.findAll().size();
        roleRole.setId(count.incrementAndGet());

        // Create the RoleRole
        RoleRoleDTO roleRoleDTO = roleRoleMapper.toDto(roleRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleRoleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roleRoleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleRole in the database
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleRole() throws Exception {
        // Initialize the database
        roleRoleRepository.saveAndFlush(roleRole);

        int databaseSizeBeforeDelete = roleRoleRepository.findAll().size();

        // Delete the roleRole
        restRoleRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleRole> roleRoleList = roleRoleRepository.findAll();
        assertThat(roleRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
