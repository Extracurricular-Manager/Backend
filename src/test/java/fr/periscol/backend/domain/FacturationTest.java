package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facturation.class);
        Facturation facturation1 = new Facturation();
        facturation1.setId(1L);
        Facturation facturation2 = new Facturation();
        facturation2.setId(facturation1.getId());
        assertThat(facturation1).isEqualTo(facturation2);
        facturation2.setId(2L);
        assertThat(facturation1).isNotEqualTo(facturation2);
        facturation1.setId(null);
        assertThat(facturation1).isNotEqualTo(facturation2);
    }
}
