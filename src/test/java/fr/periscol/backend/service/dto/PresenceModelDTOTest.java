package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresenceModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresenceModelDTO.class);
        PresenceModelDTO presenceModelDTO1 = new PresenceModelDTO();
        presenceModelDTO1.setId(1L);
        PresenceModelDTO presenceModelDTO2 = new PresenceModelDTO();
        assertThat(presenceModelDTO1).isNotEqualTo(presenceModelDTO2);
        presenceModelDTO2.setId(presenceModelDTO1.getId());
        assertThat(presenceModelDTO1).isEqualTo(presenceModelDTO2);
        presenceModelDTO2.setId(2L);
        assertThat(presenceModelDTO1).isNotEqualTo(presenceModelDTO2);
        presenceModelDTO1.setId(null);
        assertThat(presenceModelDTO1).isNotEqualTo(presenceModelDTO2);
    }
}
