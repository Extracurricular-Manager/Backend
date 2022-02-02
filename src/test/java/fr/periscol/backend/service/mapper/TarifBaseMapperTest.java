package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TarifBaseMapperTest {

    private TarifBaseMapper tarifBaseMapper;

    @BeforeEach
    public void setUp() {
        tarifBaseMapper = new TarifBaseMapperImpl();
    }
}
