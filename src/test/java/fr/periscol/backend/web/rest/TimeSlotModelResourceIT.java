package fr.periscol.backend.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.periscol.backend.IntegrationTest;
import fr.periscol.backend.domain.TimeSlotModel;
import fr.periscol.backend.repository.TimeSlotModelRepository;
import fr.periscol.backend.service.dto.TimeSlotModelDTO;
import fr.periscol.backend.service.mapper.TimeSlotModelMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TimeSlotModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSlotModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TIME_OF_ARRIVAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_OF_ARRIVAL = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TIME_OF_DEPARTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIME_OF_DEPARTURE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/time-slot-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeSlotModelRepository timeSlotModelRepository;

    @Autowired
    private TimeSlotModelMapper timeSlotModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSlotModelMockMvc;

    private TimeSlotModel timeSlotModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlotModel createEntity(EntityManager em) {
        TimeSlotModel timeSlotModel = new TimeSlotModel()
            .name(DEFAULT_NAME)
            .timeOfArrival(DEFAULT_TIME_OF_ARRIVAL)
            .timeOfDeparture(DEFAULT_TIME_OF_DEPARTURE);
        return timeSlotModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSlotModel createUpdatedEntity(EntityManager em) {
        TimeSlotModel timeSlotModel = new TimeSlotModel()
            .name(UPDATED_NAME)
            .timeOfArrival(UPDATED_TIME_OF_ARRIVAL)
            .timeOfDeparture(UPDATED_TIME_OF_DEPARTURE);
        return timeSlotModel;
    }

    @BeforeEach
    public void initTest() {
        timeSlotModel = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeSlotModel() throws Exception {
        int databaseSizeBeforeCreate = timeSlotModelRepository.findAll().size();
        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);
        restTimeSlotModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeCreate + 1);
        TimeSlotModel testTimeSlotModel = timeSlotModelList.get(timeSlotModelList.size() - 1);
        assertThat(testTimeSlotModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTimeSlotModel.getTimeOfArrival()).isEqualTo(DEFAULT_TIME_OF_ARRIVAL);
        assertThat(testTimeSlotModel.getTimeOfDeparture()).isEqualTo(DEFAULT_TIME_OF_DEPARTURE);
    }

    @Test
    @Transactional
    void createTimeSlotModelWithExistingId() throws Exception {
        // Create the TimeSlotModel with an existing ID
        timeSlotModel.setId(1L);
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        int databaseSizeBeforeCreate = timeSlotModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSlotModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTimeSlotModels() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        // Get all the timeSlotModelList
        restTimeSlotModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSlotModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].timeOfArrival").value(hasItem(DEFAULT_TIME_OF_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].timeOfDeparture").value(hasItem(DEFAULT_TIME_OF_DEPARTURE.toString())));
    }

    @Test
    @Transactional
    void getTimeSlotModel() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        // Get the timeSlotModel
        restTimeSlotModelMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSlotModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSlotModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.timeOfArrival").value(DEFAULT_TIME_OF_ARRIVAL.toString()))
            .andExpect(jsonPath("$.timeOfDeparture").value(DEFAULT_TIME_OF_DEPARTURE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeSlotModel() throws Exception {
        // Get the timeSlotModel
        restTimeSlotModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTimeSlotModel() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();

        // Update the timeSlotModel
        TimeSlotModel updatedTimeSlotModel = timeSlotModelRepository.findById(timeSlotModel.getId()).get();
        // Disconnect from session so that the updates on updatedTimeSlotModel are not directly saved in db
        em.detach(updatedTimeSlotModel);
        updatedTimeSlotModel.name(UPDATED_NAME).timeOfArrival(UPDATED_TIME_OF_ARRIVAL).timeOfDeparture(UPDATED_TIME_OF_DEPARTURE);
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(updatedTimeSlotModel);

        restTimeSlotModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSlotModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
        TimeSlotModel testTimeSlotModel = timeSlotModelList.get(timeSlotModelList.size() - 1);
        assertThat(testTimeSlotModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimeSlotModel.getTimeOfArrival()).isEqualTo(UPDATED_TIME_OF_ARRIVAL);
        assertThat(testTimeSlotModel.getTimeOfDeparture()).isEqualTo(UPDATED_TIME_OF_DEPARTURE);
    }

    @Test
    @Transactional
    void putNonExistingTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSlotModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSlotModelWithPatch() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();

        // Update the timeSlotModel using partial update
        TimeSlotModel partialUpdatedTimeSlotModel = new TimeSlotModel();
        partialUpdatedTimeSlotModel.setId(timeSlotModel.getId());

        partialUpdatedTimeSlotModel.name(UPDATED_NAME).timeOfArrival(UPDATED_TIME_OF_ARRIVAL);

        restTimeSlotModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlotModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSlotModel))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
        TimeSlotModel testTimeSlotModel = timeSlotModelList.get(timeSlotModelList.size() - 1);
        assertThat(testTimeSlotModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimeSlotModel.getTimeOfArrival()).isEqualTo(UPDATED_TIME_OF_ARRIVAL);
        assertThat(testTimeSlotModel.getTimeOfDeparture()).isEqualTo(DEFAULT_TIME_OF_DEPARTURE);
    }

    @Test
    @Transactional
    void fullUpdateTimeSlotModelWithPatch() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();

        // Update the timeSlotModel using partial update
        TimeSlotModel partialUpdatedTimeSlotModel = new TimeSlotModel();
        partialUpdatedTimeSlotModel.setId(timeSlotModel.getId());

        partialUpdatedTimeSlotModel.name(UPDATED_NAME).timeOfArrival(UPDATED_TIME_OF_ARRIVAL).timeOfDeparture(UPDATED_TIME_OF_DEPARTURE);

        restTimeSlotModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSlotModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeSlotModel))
            )
            .andExpect(status().isOk());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
        TimeSlotModel testTimeSlotModel = timeSlotModelList.get(timeSlotModelList.size() - 1);
        assertThat(testTimeSlotModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimeSlotModel.getTimeOfArrival()).isEqualTo(UPDATED_TIME_OF_ARRIVAL);
        assertThat(testTimeSlotModel.getTimeOfDeparture()).isEqualTo(UPDATED_TIME_OF_DEPARTURE);
    }

    @Test
    @Transactional
    void patchNonExistingTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSlotModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSlotModel() throws Exception {
        int databaseSizeBeforeUpdate = timeSlotModelRepository.findAll().size();
        timeSlotModel.setId(count.incrementAndGet());

        // Create the TimeSlotModel
        TimeSlotModelDTO timeSlotModelDTO = timeSlotModelMapper.toDto(timeSlotModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSlotModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeSlotModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSlotModel in the database
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSlotModel() throws Exception {
        // Initialize the database
        timeSlotModelRepository.saveAndFlush(timeSlotModel);

        int databaseSizeBeforeDelete = timeSlotModelRepository.findAll().size();

        // Delete the timeSlotModel
        restTimeSlotModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSlotModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TimeSlotModel> timeSlotModelList = timeSlotModelRepository.findAll();
        assertThat(timeSlotModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
