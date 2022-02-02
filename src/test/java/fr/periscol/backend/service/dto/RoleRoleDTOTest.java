package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleRoleDTO.class);
        RoleRoleDTO roleRoleDTO1 = new RoleRoleDTO();
        roleRoleDTO1.setId(1L);
        RoleRoleDTO roleRoleDTO2 = new RoleRoleDTO();
        assertThat(roleRoleDTO1).isNotEqualTo(roleRoleDTO2);
        roleRoleDTO2.setId(roleRoleDTO1.getId());
        assertThat(roleRoleDTO1).isEqualTo(roleRoleDTO2);
        roleRoleDTO2.setId(2L);
        assertThat(roleRoleDTO1).isNotEqualTo(roleRoleDTO2);
        roleRoleDTO1.setId(null);
        assertThat(roleRoleDTO1).isNotEqualTo(roleRoleDTO2);
    }
}
