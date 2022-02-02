package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacturationMapperTest {

    private FacturationMapper facturationMapper;

    @BeforeEach
    public void setUp() {
        facturationMapper = new FacturationMapperImpl();
    }
}
