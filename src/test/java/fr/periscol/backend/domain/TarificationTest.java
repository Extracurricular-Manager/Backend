package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarification.class);
        Tarification tarification1 = new Tarification();
        tarification1.setId(1L);
        Tarification tarification2 = new Tarification();
        tarification2.setId(tarification1.getId());
        assertThat(tarification1).isEqualTo(tarification2);
        tarification2.setId(2L);
        assertThat(tarification1).isNotEqualTo(tarification2);
        tarification1.setId(null);
        assertThat(tarification1).isNotEqualTo(tarification2);
    }
}
