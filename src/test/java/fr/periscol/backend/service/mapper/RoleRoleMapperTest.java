package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleRoleMapperTest {

    private RoleRoleMapper roleRoleMapper;

    @BeforeEach
    public void setUp() {
        roleRoleMapper = new RoleRoleMapperImpl();
    }
}
