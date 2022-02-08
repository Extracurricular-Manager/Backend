package fr.periscol.backend.security;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.web.rest.WithUnauthenticatedMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithUnauthenticatedMockUser
class UserPermissionsIT {

    private static final String ENTITY_API_URL = "/api/authenticate";

    @Autowired
    private MockMvc mockMvc;

    private String getAdminToken() throws Exception {
        String JSONtoken = mockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"admin\"}")).andReturn().getResponse().getContentAsString();
        return (JSONtoken.split(":")[1]).split("\"")[1];
    }

    private String getUserToken() throws Exception {
        String JSONtoken = mockMvc.perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"user\",\"password\":\"admin\"}")).andReturn().getResponse().getContentAsString();
        return (JSONtoken.split(":")[1]).split("\"")[1];
    }

    @Test
    @Transactional
    void testPermissionAdminOK() throws Exception {
        String token = getAdminToken();
        mockMvc.perform(get("/api/children").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testPermissionUserOK() throws Exception{
        String token = getUserToken();
        mockMvc.perform(get("/api/children").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testPermissionNOTOK() throws Exception {
        mockMvc.perform(get("/api/children"))
                .andExpect(status().isUnauthorized());
        //assert(true);
    }

}
