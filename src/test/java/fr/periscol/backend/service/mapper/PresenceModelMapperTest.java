package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresenceModelMapperTest {

    private PresenceModelMapper presenceModelMapper;

    @BeforeEach
    public void setUp() {
        presenceModelMapper = new PresenceModelMapperImpl();
    }
}
