package fr.periscol.backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.periscol.backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServiceMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceMetadata.class);
        ServiceMetadata serviceMetadata1 = new ServiceMetadata();
        serviceMetadata1.setId(1L);
        ServiceMetadata serviceMetadata2 = new ServiceMetadata();
        serviceMetadata2.setId(serviceMetadata1.getId());
        assertThat(serviceMetadata1).isEqualTo(serviceMetadata2);
        serviceMetadata2.setId(2L);
        assertThat(serviceMetadata1).isNotEqualTo(serviceMetadata2);
        serviceMetadata1.setId(null);
        assertThat(serviceMetadata1).isNotEqualTo(serviceMetadata2);
    }
}
