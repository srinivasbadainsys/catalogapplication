package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FacilityOrganizationalUnit;
import com.mycompany.myapp.repository.FacilityOrganizationalUnitRepository;
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
 * Integration tests for the {@link FacilityOrganizationalUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacilityOrganizationalUnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facility-organizational-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacilityOrganizationalUnitRepository facilityOrganizationalUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilityOrganizationalUnitMockMvc;

    private FacilityOrganizationalUnit facilityOrganizationalUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityOrganizationalUnit createEntity(EntityManager em) {
        FacilityOrganizationalUnit facilityOrganizationalUnit = new FacilityOrganizationalUnit()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .code(DEFAULT_CODE);
        return facilityOrganizationalUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacilityOrganizationalUnit createUpdatedEntity(EntityManager em) {
        FacilityOrganizationalUnit facilityOrganizationalUnit = new FacilityOrganizationalUnit()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .code(UPDATED_CODE);
        return facilityOrganizationalUnit;
    }

    @BeforeEach
    public void initTest() {
        facilityOrganizationalUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeCreate = facilityOrganizationalUnitRepository.findAll().size();
        // Create the FacilityOrganizationalUnit
        restFacilityOrganizationalUnitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isCreated());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeCreate + 1);
        FacilityOrganizationalUnit testFacilityOrganizationalUnit = facilityOrganizationalUnitList.get(
            facilityOrganizationalUnitList.size() - 1
        );
        assertThat(testFacilityOrganizationalUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFacilityOrganizationalUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFacilityOrganizationalUnit.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createFacilityOrganizationalUnitWithExistingId() throws Exception {
        // Create the FacilityOrganizationalUnit with an existing ID
        facilityOrganizationalUnit.setId(1L);

        int databaseSizeBeforeCreate = facilityOrganizationalUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilityOrganizationalUnitMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacilityOrganizationalUnits() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        // Get all the facilityOrganizationalUnitList
        restFacilityOrganizationalUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilityOrganizationalUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getFacilityOrganizationalUnit() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        // Get the facilityOrganizationalUnit
        restFacilityOrganizationalUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, facilityOrganizationalUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilityOrganizationalUnit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingFacilityOrganizationalUnit() throws Exception {
        // Get the facilityOrganizationalUnit
        restFacilityOrganizationalUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacilityOrganizationalUnit() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();

        // Update the facilityOrganizationalUnit
        FacilityOrganizationalUnit updatedFacilityOrganizationalUnit = facilityOrganizationalUnitRepository
            .findById(facilityOrganizationalUnit.getId())
            .get();
        // Disconnect from session so that the updates on updatedFacilityOrganizationalUnit are not directly saved in db
        em.detach(updatedFacilityOrganizationalUnit);
        updatedFacilityOrganizationalUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restFacilityOrganizationalUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilityOrganizationalUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacilityOrganizationalUnit))
            )
            .andExpect(status().isOk());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
        FacilityOrganizationalUnit testFacilityOrganizationalUnit = facilityOrganizationalUnitList.get(
            facilityOrganizationalUnitList.size() - 1
        );
        assertThat(testFacilityOrganizationalUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFacilityOrganizationalUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFacilityOrganizationalUnit.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilityOrganizationalUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilityOrganizationalUnitWithPatch() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();

        // Update the facilityOrganizationalUnit using partial update
        FacilityOrganizationalUnit partialUpdatedFacilityOrganizationalUnit = new FacilityOrganizationalUnit();
        partialUpdatedFacilityOrganizationalUnit.setId(facilityOrganizationalUnit.getId());

        partialUpdatedFacilityOrganizationalUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFacilityOrganizationalUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityOrganizationalUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilityOrganizationalUnit))
            )
            .andExpect(status().isOk());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
        FacilityOrganizationalUnit testFacilityOrganizationalUnit = facilityOrganizationalUnitList.get(
            facilityOrganizationalUnitList.size() - 1
        );
        assertThat(testFacilityOrganizationalUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFacilityOrganizationalUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFacilityOrganizationalUnit.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFacilityOrganizationalUnitWithPatch() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();

        // Update the facilityOrganizationalUnit using partial update
        FacilityOrganizationalUnit partialUpdatedFacilityOrganizationalUnit = new FacilityOrganizationalUnit();
        partialUpdatedFacilityOrganizationalUnit.setId(facilityOrganizationalUnit.getId());

        partialUpdatedFacilityOrganizationalUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restFacilityOrganizationalUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilityOrganizationalUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilityOrganizationalUnit))
            )
            .andExpect(status().isOk());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
        FacilityOrganizationalUnit testFacilityOrganizationalUnit = facilityOrganizationalUnitList.get(
            facilityOrganizationalUnitList.size() - 1
        );
        assertThat(testFacilityOrganizationalUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFacilityOrganizationalUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFacilityOrganizationalUnit.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilityOrganizationalUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilityOrganizationalUnit() throws Exception {
        int databaseSizeBeforeUpdate = facilityOrganizationalUnitRepository.findAll().size();
        facilityOrganizationalUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilityOrganizationalUnitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facilityOrganizationalUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacilityOrganizationalUnit in the database
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilityOrganizationalUnit() throws Exception {
        // Initialize the database
        facilityOrganizationalUnitRepository.saveAndFlush(facilityOrganizationalUnit);

        int databaseSizeBeforeDelete = facilityOrganizationalUnitRepository.findAll().size();

        // Delete the facilityOrganizationalUnit
        restFacilityOrganizationalUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilityOrganizationalUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacilityOrganizationalUnit> facilityOrganizationalUnitList = facilityOrganizationalUnitRepository.findAll();
        assertThat(facilityOrganizationalUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
