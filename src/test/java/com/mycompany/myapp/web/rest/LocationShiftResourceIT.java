package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LocationShift;
import com.mycompany.myapp.repository.LocationShiftRepository;
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
 * Integration tests for the {@link LocationShiftResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationShiftResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-shifts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationShiftRepository locationShiftRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationShiftMockMvc;

    private LocationShift locationShift;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationShift createEntity(EntityManager em) {
        LocationShift locationShift = new LocationShift().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return locationShift;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationShift createUpdatedEntity(EntityManager em) {
        LocationShift locationShift = new LocationShift().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return locationShift;
    }

    @BeforeEach
    public void initTest() {
        locationShift = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationShift() throws Exception {
        int databaseSizeBeforeCreate = locationShiftRepository.findAll().size();
        // Create the LocationShift
        restLocationShiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationShift)))
            .andExpect(status().isCreated());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeCreate + 1);
        LocationShift testLocationShift = locationShiftList.get(locationShiftList.size() - 1);
        assertThat(testLocationShift.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLocationShift.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLocationShift.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createLocationShiftWithExistingId() throws Exception {
        // Create the LocationShift with an existing ID
        locationShift.setId(1L);

        int databaseSizeBeforeCreate = locationShiftRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationShiftMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationShift)))
            .andExpect(status().isBadRequest());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationShifts() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        // Get all the locationShiftList
        restLocationShiftMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationShift.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getLocationShift() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        // Get the locationShift
        restLocationShiftMockMvc
            .perform(get(ENTITY_API_URL_ID, locationShift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationShift.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingLocationShift() throws Exception {
        // Get the locationShift
        restLocationShiftMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocationShift() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();

        // Update the locationShift
        LocationShift updatedLocationShift = locationShiftRepository.findById(locationShift.getId()).get();
        // Disconnect from session so that the updates on updatedLocationShift are not directly saved in db
        em.detach(updatedLocationShift);
        updatedLocationShift.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restLocationShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationShift.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationShift))
            )
            .andExpect(status().isOk());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
        LocationShift testLocationShift = locationShiftList.get(locationShiftList.size() - 1);
        assertThat(testLocationShift.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLocationShift.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLocationShift.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationShift.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationShift))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationShift))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationShift)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationShiftWithPatch() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();

        // Update the locationShift using partial update
        LocationShift partialUpdatedLocationShift = new LocationShift();
        partialUpdatedLocationShift.setId(locationShift.getId());

        partialUpdatedLocationShift.name(UPDATED_NAME).code(UPDATED_CODE);

        restLocationShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationShift.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationShift))
            )
            .andExpect(status().isOk());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
        LocationShift testLocationShift = locationShiftList.get(locationShiftList.size() - 1);
        assertThat(testLocationShift.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLocationShift.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLocationShift.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateLocationShiftWithPatch() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();

        // Update the locationShift using partial update
        LocationShift partialUpdatedLocationShift = new LocationShift();
        partialUpdatedLocationShift.setId(locationShift.getId());

        partialUpdatedLocationShift.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restLocationShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationShift.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationShift))
            )
            .andExpect(status().isOk());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
        LocationShift testLocationShift = locationShiftList.get(locationShiftList.size() - 1);
        assertThat(testLocationShift.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLocationShift.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLocationShift.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationShift.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationShift))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationShift))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationShift() throws Exception {
        int databaseSizeBeforeUpdate = locationShiftRepository.findAll().size();
        locationShift.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationShiftMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(locationShift))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationShift in the database
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationShift() throws Exception {
        // Initialize the database
        locationShiftRepository.saveAndFlush(locationShift);

        int databaseSizeBeforeDelete = locationShiftRepository.findAll().size();

        // Delete the locationShift
        restLocationShiftMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationShift.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationShift> locationShiftList = locationShiftRepository.findAll();
        assertThat(locationShiftList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
