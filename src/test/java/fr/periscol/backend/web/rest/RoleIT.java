package fr.periscol.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Permission;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.PermissionRepository;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.service.PermissionService;
import fr.periscol.backend.service.RoleService;
import fr.periscol.backend.service.dto.PermissionDTO;
import fr.periscol.backend.service.dto.RoleDTO;
import fr.periscol.backend.service.mapper.PermissionMapper;
import static org.assertj.core.api.Assertions.assertThat;
import fr.periscol.backend.service.mapper.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoleIT {

    private static final String ENTITY_API_URL_BEGINNING = "/api/role-roles/";
    private static final String ENTITY_API_URL_ENDING = "/permission";

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository proleRepositoryMock;

    @Autowired
    private RoleMapper roleMapper;

    @Mock
    private RoleService roleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleMockMvc;

    @Autowired
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionRepository permissionServiceMock;

    @Autowired
    private PermissionMapper permissionMapper;

    private Role role;

    private Permission permission;

    private Role createEntityRole(EntityManager em){
        final var role = new Role();
        role.setPermissions(List.of(new Permission("perm1"), new Permission("perm2")));
        role.setName("ROLE_PERSO");
        return role;
    }

    private Permission createEntityPermission(EntityManager em){
        return new Permission("perm3");
    }

    @BeforeEach
    public void initTest() {
        role = createEntityRole(em);
        permission = createEntityPermission(em);
    }

    @Test
    @Transactional
    public void getPermissionFromRole() throws Exception{
        roleRepository.saveAndFlush(role);
        String id = role.getName();
        restRoleMockMvc.perform(get(ENTITY_API_URL_BEGINNING + id + ENTITY_API_URL_ENDING))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].name").value(role.getPermissions().get(0).getName()))
                .andExpect(jsonPath("$.[1].name").value(role.getPermissions().get(1).getName()));
    }

    @Test
    @Transactional
    public void getPermissionFromAbsentRole() throws Exception{
        String id = "____ROLE_NON_EXISTING";
        restRoleMockMvc.perform(get(ENTITY_API_URL_BEGINNING + id + ENTITY_API_URL_ENDING))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void addPermissionFromRole() throws Exception{
        roleRepository.saveAndFlush(role);
        permissionRepository.saveAndFlush(permission);
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();
        String permissionJson = new ObjectMapper().writeValueAsString(permission);
        restRoleMockMvc.perform(patch(ENTITY_API_URL_BEGINNING + role.getName() + ENTITY_API_URL_ENDING)
                        .contentType("application/json")
                        .content(permissionJson))
                .andExpect(status().isCreated());
        List<Role> roleList = roleRepository.findAll();
        assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
        int index = roleList.indexOf(role);
        assert(index != -1);
        Role roleDatabase = roleList.get(index);
        Permission permissionDataBase = roleDatabase.getPermissions().get(roleDatabase.getPermissions().size() - 1);
        assertThat(permissionDataBase).isEqualTo(permission);
    }

    @Test
    @Transactional
    public void deletePermissionFromRole() throws Exception{
        roleRepository.saveAndFlush(role);
        String namePermissionToDelete = "perm2";
        restRoleMockMvc.perform(delete(ENTITY_API_URL_BEGINNING + role.getName() + ENTITY_API_URL_ENDING + "/" + namePermissionToDelete))
                .andExpect(status().isNoContent());
        List<Role> roleList = roleRepository.findAll();
        int index = roleList.indexOf(role);
        assert(index != -1);
        Role roleDatabase = roleList.get(index);
        List<Permission> permissionList = roleDatabase.getPermissions();
        assertThat(permissionList).isNotEmpty().doesNotContain(new Permission("perm2"));
    }

    @Test
    @Transactional
    public void deleteAbsentPermissionFromRole() throws Exception{
        roleRepository.saveAndFlush(role);
        String namePermissionToDelete = "toto";
        restRoleMockMvc.perform(delete(ENTITY_API_URL_BEGINNING + role.getName() + ENTITY_API_URL_ENDING + "/" + namePermissionToDelete))
                .andExpect(status().isBadRequest());
    }
}
