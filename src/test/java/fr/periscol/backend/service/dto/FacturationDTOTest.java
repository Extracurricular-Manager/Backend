package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturationDTO.class);
        FacturationDTO facturationDTO1 = new FacturationDTO();
        facturationDTO1.setId(1L);
        FacturationDTO facturationDTO2 = new FacturationDTO();
        assertThat(facturationDTO1).isNotEqualTo(facturationDTO2);
        facturationDTO2.setId(facturationDTO1.getId());
        assertThat(facturationDTO1).isEqualTo(facturationDTO2);
        facturationDTO2.setId(2L);
        assertThat(facturationDTO1).isNotEqualTo(facturationDTO2);
        facturationDTO1.setId(null);
        assertThat(facturationDTO1).isNotEqualTo(facturationDTO2);
    }
}
