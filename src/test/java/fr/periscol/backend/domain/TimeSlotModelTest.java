package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSlotModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSlotModel.class);
        TimeSlotModel timeSlotModel1 = new TimeSlotModel();
        timeSlotModel1.setId(1L);
        TimeSlotModel timeSlotModel2 = new TimeSlotModel();
        timeSlotModel2.setId(timeSlotModel1.getId());
        assertThat(timeSlotModel1).isEqualTo(timeSlotModel2);
        timeSlotModel2.setId(2L);
        assertThat(timeSlotModel1).isNotEqualTo(timeSlotModel2);
        timeSlotModel1.setId(null);
        assertThat(timeSlotModel1).isNotEqualTo(timeSlotModel2);
    }
}
