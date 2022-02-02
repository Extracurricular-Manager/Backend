package fr.periscol.backend.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeSlotModelMapperTest {

    private TimeSlotModelMapper timeSlotModelMapper;

    @BeforeEach
    public void setUp() {
        timeSlotModelMapper = new TimeSlotModelMapperImpl();
    }
}
