package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCustomMapperTest {

    private UserCustomMapper userCustomMapper;

    @BeforeEach
    public void setUp() {
        userCustomMapper = new UserCustomMapperImpl();
    }
}
