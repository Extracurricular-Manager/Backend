package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarificationDTO.class);
        TarificationDTO tarificationDTO1 = new TarificationDTO();
        tarificationDTO1.setId(1L);
        TarificationDTO tarificationDTO2 = new TarificationDTO();
        assertThat(tarificationDTO1).isNotEqualTo(tarificationDTO2);
        tarificationDTO2.setId(tarificationDTO1.getId());
        assertThat(tarificationDTO1).isEqualTo(tarificationDTO2);
        tarificationDTO2.setId(2L);
        assertThat(tarificationDTO1).isNotEqualTo(tarificationDTO2);
        tarificationDTO1.setId(null);
        assertThat(tarificationDTO1).isNotEqualTo(tarificationDTO2);
    }
}
