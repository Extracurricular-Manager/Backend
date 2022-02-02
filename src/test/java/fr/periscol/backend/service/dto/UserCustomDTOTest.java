package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCustomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCustomDTO.class);
        UserCustomDTO userCustomDTO1 = new UserCustomDTO();
        userCustomDTO1.setId(1L);
        UserCustomDTO userCustomDTO2 = new UserCustomDTO();
        assertThat(userCustomDTO1).isNotEqualTo(userCustomDTO2);
        userCustomDTO2.setId(userCustomDTO1.getId());
        assertThat(userCustomDTO1).isEqualTo(userCustomDTO2);
        userCustomDTO2.setId(2L);
        assertThat(userCustomDTO1).isNotEqualTo(userCustomDTO2);
        userCustomDTO1.setId(null);
        assertThat(userCustomDTO1).isNotEqualTo(userCustomDTO2);
    }
}
