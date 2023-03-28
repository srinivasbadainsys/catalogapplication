package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompPlanPayrollType;
import com.mycompany.myapp.repository.CompPlanPayrollTypeRepository;
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
 * Integration tests for the {@link CompPlanPayrollTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompPlanPayrollTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comp-plan-payroll-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompPlanPayrollTypeRepository compPlanPayrollTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompPlanPayrollTypeMockMvc;

    private CompPlanPayrollType compPlanPayrollType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompPlanPayrollType createEntity(EntityManager em) {
        CompPlanPayrollType compPlanPayrollType = new CompPlanPayrollType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE);
        return compPlanPayrollType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompPlanPayrollType createUpdatedEntity(EntityManager em) {
        CompPlanPayrollType compPlanPayrollType = new CompPlanPayrollType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE);
        return compPlanPayrollType;
    }

    @BeforeEach
    public void initTest() {
        compPlanPayrollType = createEntity(em);
    }

    @Test
    @Transactional
    void createCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeCreate = compPlanPayrollTypeRepository.findAll().size();
        // Create the CompPlanPayrollType
        restCompPlanPayrollTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isCreated());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CompPlanPayrollType testCompPlanPayrollType = compPlanPayrollTypeList.get(compPlanPayrollTypeList.size() - 1);
        assertThat(testCompPlanPayrollType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompPlanPayrollType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompPlanPayrollType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createCompPlanPayrollTypeWithExistingId() throws Exception {
        // Create the CompPlanPayrollType with an existing ID
        compPlanPayrollType.setId(1L);

        int databaseSizeBeforeCreate = compPlanPayrollTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompPlanPayrollTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompPlanPayrollTypes() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        // Get all the compPlanPayrollTypeList
        restCompPlanPayrollTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compPlanPayrollType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getCompPlanPayrollType() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        // Get the compPlanPayrollType
        restCompPlanPayrollTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, compPlanPayrollType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compPlanPayrollType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingCompPlanPayrollType() throws Exception {
        // Get the compPlanPayrollType
        restCompPlanPayrollTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompPlanPayrollType() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();

        // Update the compPlanPayrollType
        CompPlanPayrollType updatedCompPlanPayrollType = compPlanPayrollTypeRepository.findById(compPlanPayrollType.getId()).get();
        // Disconnect from session so that the updates on updatedCompPlanPayrollType are not directly saved in db
        em.detach(updatedCompPlanPayrollType);
        updatedCompPlanPayrollType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restCompPlanPayrollTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompPlanPayrollType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompPlanPayrollType))
            )
            .andExpect(status().isOk());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
        CompPlanPayrollType testCompPlanPayrollType = compPlanPayrollTypeList.get(compPlanPayrollTypeList.size() - 1);
        assertThat(testCompPlanPayrollType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompPlanPayrollType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompPlanPayrollType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compPlanPayrollType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompPlanPayrollTypeWithPatch() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();

        // Update the compPlanPayrollType using partial update
        CompPlanPayrollType partialUpdatedCompPlanPayrollType = new CompPlanPayrollType();
        partialUpdatedCompPlanPayrollType.setId(compPlanPayrollType.getId());

        partialUpdatedCompPlanPayrollType.name(UPDATED_NAME).code(UPDATED_CODE);

        restCompPlanPayrollTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompPlanPayrollType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompPlanPayrollType))
            )
            .andExpect(status().isOk());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
        CompPlanPayrollType testCompPlanPayrollType = compPlanPayrollTypeList.get(compPlanPayrollTypeList.size() - 1);
        assertThat(testCompPlanPayrollType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompPlanPayrollType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompPlanPayrollType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCompPlanPayrollTypeWithPatch() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();

        // Update the compPlanPayrollType using partial update
        CompPlanPayrollType partialUpdatedCompPlanPayrollType = new CompPlanPayrollType();
        partialUpdatedCompPlanPayrollType.setId(compPlanPayrollType.getId());

        partialUpdatedCompPlanPayrollType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restCompPlanPayrollTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompPlanPayrollType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompPlanPayrollType))
            )
            .andExpect(status().isOk());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
        CompPlanPayrollType testCompPlanPayrollType = compPlanPayrollTypeList.get(compPlanPayrollTypeList.size() - 1);
        assertThat(testCompPlanPayrollType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompPlanPayrollType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompPlanPayrollType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compPlanPayrollType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompPlanPayrollType() throws Exception {
        int databaseSizeBeforeUpdate = compPlanPayrollTypeRepository.findAll().size();
        compPlanPayrollType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompPlanPayrollTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compPlanPayrollType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompPlanPayrollType in the database
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompPlanPayrollType() throws Exception {
        // Initialize the database
        compPlanPayrollTypeRepository.saveAndFlush(compPlanPayrollType);

        int databaseSizeBeforeDelete = compPlanPayrollTypeRepository.findAll().size();

        // Delete the compPlanPayrollType
        restCompPlanPayrollTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, compPlanPayrollType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompPlanPayrollType> compPlanPayrollTypeList = compPlanPayrollTypeRepository.findAll();
        assertThat(compPlanPayrollTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
