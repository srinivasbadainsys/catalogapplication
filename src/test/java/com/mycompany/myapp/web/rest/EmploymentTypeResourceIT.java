package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmploymentType;
import com.mycompany.myapp.repository.EmploymentTypeRepository;
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
 * Integration tests for the {@link EmploymentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentTypeRepository employmentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentTypeMockMvc;

    private EmploymentType employmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentType createEntity(EntityManager em) {
        EmploymentType employmentType = new EmploymentType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return employmentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentType createUpdatedEntity(EntityManager em) {
        EmploymentType employmentType = new EmploymentType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return employmentType;
    }

    @BeforeEach
    public void initTest() {
        employmentType = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentType() throws Exception {
        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();
        // Create the EmploymentType
        restEmploymentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmploymentType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmploymentType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createEmploymentTypeWithExistingId() throws Exception {
        // Create the EmploymentType with an existing ID
        employmentType.setId(1L);

        int databaseSizeBeforeCreate = employmentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmploymentTypes() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get all the employmentTypeList
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        // Get the employmentType
        restEmploymentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentType() throws Exception {
        // Get the employmentType
        restEmploymentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType
        EmploymentType updatedEmploymentType = employmentTypeRepository.findById(employmentType.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentType are not directly saved in db
        em.detach(updatedEmploymentType);
        updatedEmploymentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmploymentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmploymentType))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmploymentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentTypeWithPatch() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType using partial update
        EmploymentType partialUpdatedEmploymentType = new EmploymentType();
        partialUpdatedEmploymentType.setId(employmentType.getId());

        partialUpdatedEmploymentType.description(UPDATED_DESCRIPTION);

        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentType))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmploymentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentTypeWithPatch() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();

        // Update the employmentType using partial update
        EmploymentType partialUpdatedEmploymentType = new EmploymentType();
        partialUpdatedEmploymentType.setId(employmentType.getId());

        partialUpdatedEmploymentType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentType))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
        EmploymentType testEmploymentType = employmentTypeList.get(employmentTypeList.size() - 1);
        assertThat(testEmploymentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmploymentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmploymentType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentType() throws Exception {
        int databaseSizeBeforeUpdate = employmentTypeRepository.findAll().size();
        employmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employmentType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentType in the database
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentType() throws Exception {
        // Initialize the database
        employmentTypeRepository.saveAndFlush(employmentType);

        int databaseSizeBeforeDelete = employmentTypeRepository.findAll().size();

        // Delete the employmentType
        restEmploymentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentType> employmentTypeList = employmentTypeRepository.findAll();
        assertThat(employmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
