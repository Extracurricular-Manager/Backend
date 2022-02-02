package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModifierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModifierDTO.class);
        ModifierDTO modifierDTO1 = new ModifierDTO();
        modifierDTO1.setId(1L);
        ModifierDTO modifierDTO2 = new ModifierDTO();
        assertThat(modifierDTO1).isNotEqualTo(modifierDTO2);
        modifierDTO2.setId(modifierDTO1.getId());
        assertThat(modifierDTO1).isEqualTo(modifierDTO2);
        modifierDTO2.setId(2L);
        assertThat(modifierDTO1).isNotEqualTo(modifierDTO2);
        modifierDTO1.setId(null);
        assertThat(modifierDTO1).isNotEqualTo(modifierDTO2);
    }
}
