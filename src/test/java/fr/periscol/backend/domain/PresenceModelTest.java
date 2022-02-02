package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PresenceModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresenceModel.class);
        PresenceModel presenceModel1 = new PresenceModel();
        presenceModel1.setId(1L);
        PresenceModel presenceModel2 = new PresenceModel();
        presenceModel2.setId(presenceModel1.getId());
        assertThat(presenceModel1).isEqualTo(presenceModel2);
        presenceModel2.setId(2L);
        assertThat(presenceModel1).isNotEqualTo(presenceModel2);
        presenceModel1.setId(null);
        assertThat(presenceModel1).isNotEqualTo(presenceModel2);
    }
}
