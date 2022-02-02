package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifBaseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifBaseDTO.class);
        TarifBaseDTO tarifBaseDTO1 = new TarifBaseDTO();
        tarifBaseDTO1.setId(1L);
        TarifBaseDTO tarifBaseDTO2 = new TarifBaseDTO();
        assertThat(tarifBaseDTO1).isNotEqualTo(tarifBaseDTO2);
        tarifBaseDTO2.setId(tarifBaseDTO1.getId());
        assertThat(tarifBaseDTO1).isEqualTo(tarifBaseDTO2);
        tarifBaseDTO2.setId(2L);
        assertThat(tarifBaseDTO1).isNotEqualTo(tarifBaseDTO2);
        tarifBaseDTO1.setId(null);
        assertThat(tarifBaseDTO1).isNotEqualTo(tarifBaseDTO2);
    }
}
