package fr.periscol.backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSlotModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSlotModelDTO.class);
        TimeSlotModelDTO timeSlotModelDTO1 = new TimeSlotModelDTO();
        timeSlotModelDTO1.setId(1L);
        TimeSlotModelDTO timeSlotModelDTO2 = new TimeSlotModelDTO();
        assertThat(timeSlotModelDTO1).isNotEqualTo(timeSlotModelDTO2);
        timeSlotModelDTO2.setId(timeSlotModelDTO1.getId());
        assertThat(timeSlotModelDTO1).isEqualTo(timeSlotModelDTO2);
        timeSlotModelDTO2.setId(2L);
        assertThat(timeSlotModelDTO1).isNotEqualTo(timeSlotModelDTO2);
        timeSlotModelDTO1.setId(null);
        assertThat(timeSlotModelDTO1).isNotEqualTo(timeSlotModelDTO2);
    }
}
