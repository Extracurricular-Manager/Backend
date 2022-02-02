package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModifierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modifier.class);
        Modifier modifier1 = new Modifier();
        modifier1.setId(1L);
        Modifier modifier2 = new Modifier();
        modifier2.setId(modifier1.getId());
        assertThat(modifier1).isEqualTo(modifier2);
        modifier2.setId(2L);
        assertThat(modifier1).isNotEqualTo(modifier2);
        modifier1.setId(null);
        assertThat(modifier1).isNotEqualTo(modifier2);
    }
}
