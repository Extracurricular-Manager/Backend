package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModifierMapperTest {

    private ModifierMapper modifierMapper;

    @BeforeEach
    public void setUp() {
        modifierMapper = new ModifierMapperImpl();
    }
}
