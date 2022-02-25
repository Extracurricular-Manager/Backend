package fr.periscol.backend.web.rest;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.service_model.ModelEnum;
import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.repository.service_model.PeriodModelRepository;
import fr.periscol.backend.service.ChildService;
import fr.periscol.backend.service.UserService;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.service_model.PeriodModelMapper;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"ROLE_ADMIN", "Foo"})
class PeriodModelResourceTest {

    @Autowired
    private PeriodModelRepository repository;

    @Autowired
    private ServiceMetadataService metadataService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChildService childService;

    @Autowired
    private MockMvc servicePeriodModelMockMvc;

    @Autowired
    private PeriodModelMapper mapper;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private EntityManager em;

    private Child child;
    private ServiceMetadata metadata;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String ENTITY_API_URL = "/api/period-service/";

    @Transactional
    void addServiceMetadata(EntityManager em) {
        final var metadataDTO = new NewServiceMetadataDTO();
        metadataDTO.setName("Foo");
        metadataDTO.setModel(ModelEnum.PRESENCE.getId());
        metadataDTO.setIcon("foo");
        metadata = metadataService.createServiceMetadata(metadataDTO);
    }

    @Transactional
    void addChild(EntityManager em) {
        final var child = new Child();
        child.setName("Michel");
        child.setBirthday(LocalDate.now());
        child.setSurname("Michel");
        child.setAdelphie(null);
        child.setClassroom(null);
        child.setFacturation(null);
        child.setGradeLevel(null);
        this.child = childMapper.toEntity(childService.save(childMapper.toDto(child)));
    }

    @Transactional
    PeriodModel addEntry(EntityManager em) {
        final var entry = new PeriodModel();
        entry.setId(47L);
        entry.setStartBilling(LocalDateTime.now());
        entry.setEnd(LocalDateTime.now());
        entry.setBegin(LocalDateTime.now());
        entry.setChild(child);
        entry.setServiceId(metadata.getId());
        return repository.save(entry);
    }

    @Transactional
    PeriodModel addPastEntry(EntityManager em) {
        final var entry = new PeriodModel();
        entry.setId(47L);
        entry.setStartBilling(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        entry.setEnd(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        entry.setBegin(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        entry.setChild(child);
        entry.setServiceId(metadata.getId());
        return repository.save(entry);
    }

    @BeforeEach
    void before() {
        addServiceMetadata(em);
        addChild(em);
    }

    @Test
    @Transactional
    void testAddNewEntry() throws Exception {
        final var entry = new PeriodModel();
        entry.setId(47L);
        entry.setStartBilling(LocalDateTime.now());
        entry.setEnd(LocalDateTime.now());
        entry.setBegin(LocalDateTime.now());
        entry.setChild(child);

        final var json = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build().writeValueAsString(mapper.toDto(entry));

        servicePeriodModelMockMvc.perform(put(ENTITY_API_URL + metadata.getId())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void testAddNewEntryNonExistingChild() throws Exception {
        final var child = new Child();
        child.setId(1554L);
        child.setName("Marcel");

        final var entry = new PeriodModel();
        entry.setId(0L);
        entry.setStartBilling(LocalDateTime.now());
        entry.setEnd(LocalDateTime.now());
        entry.setBegin(LocalDateTime.now());
        entry.setChild(child);

        final var json = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build().writeValueAsString(mapper.toDto(entry));

        servicePeriodModelMockMvc.perform(put(ENTITY_API_URL + metadata.getId())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void testAddNewEntryNonExistingService() throws Exception {
        final var child = new Child();
        child.setId(1554L);
        child.setName("Marcel");

        final var entry = new PeriodModel();
        entry.setId(47L);
        entry.setStartBilling(LocalDateTime.now());
        entry.setEnd(LocalDateTime.now());
        entry.setBegin(LocalDateTime.now());
        entry.setChild(child);

        final var json = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build().writeValueAsString(mapper.toDto(entry));

        servicePeriodModelMockMvc.perform(put(ENTITY_API_URL + 9999)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void testGetAllEntries() throws Exception {

        final var entry = addEntry(em);

        servicePeriodModelMockMvc.perform(get(ENTITY_API_URL + metadata.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").value(entry.getId().intValue()))
                .andExpect(jsonPath("$.[*].child.id").value(entry.getChild().getId().intValue()))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    @Transactional
    void testGetAllPastEntries() throws Exception {

        final var entry = addPastEntry(em);
        final var date = dateFormat.format(Date.from(entry.getBegin().atZone(ZoneId.systemDefault()).toInstant()));
        servicePeriodModelMockMvc.perform(get(ENTITY_API_URL + metadata.getId() + "?date=" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").value(entry.getId().intValue()))
                .andExpect(jsonPath("$.[*].child.id").value(entry.getChild().getId().intValue()))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    void testGetNotExistingService() throws Exception {
        servicePeriodModelMockMvc.perform(get(ENTITY_API_URL + 9885))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void testDeleteService() throws Exception {
        addEntry(em);
        servicePeriodModelMockMvc.perform(delete(ENTITY_API_URL + metadata.getId() + "/" + child.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void testDeleteNotExistingChild() throws Exception {
        servicePeriodModelMockMvc.perform(delete(ENTITY_API_URL + metadata.getId() + "/" + 9885))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void testDeleteNotExistingService() throws Exception {
        servicePeriodModelMockMvc.perform(delete(ENTITY_API_URL + 9885 + "/" + child.getId()))
                .andExpect(status().isNotFound());
    }
}