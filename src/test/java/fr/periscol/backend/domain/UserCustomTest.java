package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCustomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCustom.class);
        UserCustom userCustom1 = new UserCustom();
        userCustom1.setId(1L);
        UserCustom userCustom2 = new UserCustom();
        userCustom2.setId(userCustom1.getId());
        assertThat(userCustom1).isEqualTo(userCustom2);
        userCustom2.setId(2L);
        assertThat(userCustom1).isNotEqualTo(userCustom2);
        userCustom1.setId(null);
        assertThat(userCustom1).isNotEqualTo(userCustom2);
    }
}
