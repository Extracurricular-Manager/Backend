package fr.periscol.backend.web.rest;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.repository.PermissionRepository;
import fr.periscol.backend.service.PermissionService;
import fr.periscol.backend.service.mapper.PermissionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PermissionIT {

    private static final String ENTITY_API_URL = "/api/role-roles/permission/";

    @Autowired
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionRepository permissionRepositoryMock;

    @Autowired
    private PermissionMapper permissionMapper;

    @Mock
    private PermissionService permissionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionMockMvc;

    @Test
    @Transactional
    public void getPermissionFromRole() throws Exception{
        String id = "ROLE_USER";
        System.out.println(restPermissionMockMvc.perform(get(ENTITY_API_URL + id)).andReturn().getResponse().getContentAsString());
        //TODO Better test ie create permissions before and check if it is true.
    }
}
