package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleRole.class);
        RoleRole roleRole1 = new RoleRole();
        roleRole1.setId(1L);
        RoleRole roleRole2 = new RoleRole();
        roleRole2.setId(roleRole1.getId());
        assertThat(roleRole1).isEqualTo(roleRole2);
        roleRole2.setId(2L);
        assertThat(roleRole1).isNotEqualTo(roleRole2);
        roleRole1.setId(null);
        assertThat(roleRole1).isNotEqualTo(roleRole2);
    }
}
