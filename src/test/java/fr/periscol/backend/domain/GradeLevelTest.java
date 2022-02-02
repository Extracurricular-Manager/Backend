package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GradeLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GradeLevel.class);
        GradeLevel gradeLevel1 = new GradeLevel();
        gradeLevel1.setId(1L);
        GradeLevel gradeLevel2 = new GradeLevel();
        gradeLevel2.setId(gradeLevel1.getId());
        assertThat(gradeLevel1).isEqualTo(gradeLevel2);
        gradeLevel2.setId(2L);
        assertThat(gradeLevel1).isNotEqualTo(gradeLevel2);
        gradeLevel1.setId(null);
        assertThat(gradeLevel1).isNotEqualTo(gradeLevel2);
    }
}
