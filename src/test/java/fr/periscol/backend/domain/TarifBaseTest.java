package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifBaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifBase.class);
        TarifBase tarifBase1 = new TarifBase();
        tarifBase1.setId(1L);
        TarifBase tarifBase2 = new TarifBase();
        tarifBase2.setId(tarifBase1.getId());
        assertThat(tarifBase1).isEqualTo(tarifBase2);
        tarifBase2.setId(2L);
        assertThat(tarifBase1).isNotEqualTo(tarifBase2);
        tarifBase1.setId(null);
        assertThat(tarifBase1).isNotEqualTo(tarifBase2);
    }
}
