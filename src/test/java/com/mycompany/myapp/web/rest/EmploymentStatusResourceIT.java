package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmploymentStatus;
import com.mycompany.myapp.repository.EmploymentStatusRepository;
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
 * Integration tests for the {@link EmploymentStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employment-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentStatusRepository employmentStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentStatusMockMvc;

    private EmploymentStatus employmentStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentStatus createEntity(EntityManager em) {
        EmploymentStatus employmentStatus = new EmploymentStatus().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return employmentStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentStatus createUpdatedEntity(EntityManager em) {
        EmploymentStatus employmentStatus = new EmploymentStatus().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return employmentStatus;
    }

    @BeforeEach
    public void initTest() {
        employmentStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentStatus() throws Exception {
        int databaseSizeBeforeCreate = employmentStatusRepository.findAll().size();
        // Create the EmploymentStatus
        restEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentStatus testEmploymentStatus = employmentStatusList.get(employmentStatusList.size() - 1);
        assertThat(testEmploymentStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmploymentStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmploymentStatus.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createEmploymentStatusWithExistingId() throws Exception {
        // Create the EmploymentStatus with an existing ID
        employmentStatus.setId(1L);

        int databaseSizeBeforeCreate = employmentStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmploymentStatuses() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        // Get all the employmentStatusList
        restEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getEmploymentStatus() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        // Get the employmentStatus
        restEmploymentStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentStatus() throws Exception {
        // Get the employmentStatus
        restEmploymentStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploymentStatus() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();

        // Update the employmentStatus
        EmploymentStatus updatedEmploymentStatus = employmentStatusRepository.findById(employmentStatus.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentStatus are not directly saved in db
        em.detach(updatedEmploymentStatus);
        updatedEmploymentStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmploymentStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmploymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
        EmploymentStatus testEmploymentStatus = employmentStatusList.get(employmentStatusList.size() - 1);
        assertThat(testEmploymentStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmploymentStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentStatus.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentStatusWithPatch() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();

        // Update the employmentStatus using partial update
        EmploymentStatus partialUpdatedEmploymentStatus = new EmploymentStatus();
        partialUpdatedEmploymentStatus.setId(employmentStatus.getId());

        partialUpdatedEmploymentStatus.description(UPDATED_DESCRIPTION);

        restEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
        EmploymentStatus testEmploymentStatus = employmentStatusList.get(employmentStatusList.size() - 1);
        assertThat(testEmploymentStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmploymentStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentStatus.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentStatusWithPatch() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();

        // Update the employmentStatus using partial update
        EmploymentStatus partialUpdatedEmploymentStatus = new EmploymentStatus();
        partialUpdatedEmploymentStatus.setId(employmentStatus.getId());

        partialUpdatedEmploymentStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentStatus))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
        EmploymentStatus testEmploymentStatus = employmentStatusList.get(employmentStatusList.size() - 1);
        assertThat(testEmploymentStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmploymentStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentStatus.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentStatus() throws Exception {
        int databaseSizeBeforeUpdate = employmentStatusRepository.findAll().size();
        employmentStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentStatus in the database
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentStatus() throws Exception {
        // Initialize the database
        employmentStatusRepository.saveAndFlush(employmentStatus);

        int databaseSizeBeforeDelete = employmentStatusRepository.findAll().size();

        // Delete the employmentStatus
        restEmploymentStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentStatus> employmentStatusList = employmentStatusRepository.findAll();
        assertThat(employmentStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
