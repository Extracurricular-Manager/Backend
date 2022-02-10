package fr.periscol.backend.web.rest;

import fr.periscol.backend.IntegrationTest;
import static org.hamcrest.Matchers.hasItem;
import fr.periscol.backend.domain.Role;
import fr.periscol.backend.domain.User;
import fr.periscol.backend.repository.RoleRepository;
import fr.periscol.backend.repository.UserRepository;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.mapper.RoleMapper;
import fr.periscol.backend.service.mapper.UserMapper;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles={"ADMIN"})
public class UserResourceIT {

    private static final String ENTITY_API_URL_BEGINNING = "/api/user/";
    private static final String ENTITY_API_URL_ENDING = "/role/";
    private static final String ENTITY_API_URL_ENDING_SEVERAL = "/roles";

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserRepository userRepositoryMock;

    @Autowired
    private UserMapper userMapper;

    @Mock
    private UserService userServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Autowired
    private RoleMapper RoleMapper;

    private User user;

    private Role role;
//TODO password 3 char -> ne dois pas marcher
    private User createEntityUser(EntityManager em){
        User user = new User().name("Jojo").password("Lapin");
        user.setLogin("JojoBis");

        user.setActivated(true);
        userRepository.saveAndFlush(user);

        Role role1 = new Role().name("ROLE_PERSO");
        role1.setPermissions(new ArrayList<>());
        role1.setUsers(Set.of(user));


        Role role2 = new Role().name("ROLE_PERSO_BIS");
        role2.setPermissions(new ArrayList<>());
        role2.setUsers(Set.of(user));

        roleRepository.saveAndFlush(role1);
        roleRepository.saveAndFlush(role2);

        userRepository.saveAndFlush(user);
        return user;
    }

    private Role createEntityRole(EntityManager em){
        Role role = new Role().name("ROLE_PERSO_TER");
        role.setPermissions(new ArrayList<>());
        role.setUsers(new HashSet<>());
        return role;
    }

    @BeforeEach
    public void initTest() {
        user = createEntityUser(em);
        role = createEntityRole(em);
    }

    @Test
    @Transactional
    public void getRoleFromUser() throws Exception{
        String id = user.getLogin();
        Iterator<Role> iterator = user.getRoles().iterator();
        String role1Name = iterator.next().getName();
        String role2Name = iterator.next().getName();
        restUserMockMvc.perform(get(ENTITY_API_URL_BEGINNING + id + ENTITY_API_URL_ENDING_SEVERAL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].name").value(hasItem(role1Name)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(role2Name)));
    }
}
