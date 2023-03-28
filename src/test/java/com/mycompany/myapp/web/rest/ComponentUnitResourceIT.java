package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ComponentUnit;
import com.mycompany.myapp.repository.ComponentUnitRepository;
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
 * Integration tests for the {@link ComponentUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComponentUnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/component-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComponentUnitRepository componentUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComponentUnitMockMvc;

    private ComponentUnit componentUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentUnit createEntity(EntityManager em) {
        ComponentUnit componentUnit = new ComponentUnit().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return componentUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComponentUnit createUpdatedEntity(EntityManager em) {
        ComponentUnit componentUnit = new ComponentUnit().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return componentUnit;
    }

    @BeforeEach
    public void initTest() {
        componentUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createComponentUnit() throws Exception {
        int databaseSizeBeforeCreate = componentUnitRepository.findAll().size();
        // Create the ComponentUnit
        restComponentUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentUnit)))
            .andExpect(status().isCreated());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeCreate + 1);
        ComponentUnit testComponentUnit = componentUnitList.get(componentUnitList.size() - 1);
        assertThat(testComponentUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testComponentUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComponentUnit.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createComponentUnitWithExistingId() throws Exception {
        // Create the ComponentUnit with an existing ID
        componentUnit.setId(1L);

        int databaseSizeBeforeCreate = componentUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComponentUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentUnit)))
            .andExpect(status().isBadRequest());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComponentUnits() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        // Get all the componentUnitList
        restComponentUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(componentUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getComponentUnit() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        // Get the componentUnit
        restComponentUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, componentUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(componentUnit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingComponentUnit() throws Exception {
        // Get the componentUnit
        restComponentUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComponentUnit() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();

        // Update the componentUnit
        ComponentUnit updatedComponentUnit = componentUnitRepository.findById(componentUnit.getId()).get();
        // Disconnect from session so that the updates on updatedComponentUnit are not directly saved in db
        em.detach(updatedComponentUnit);
        updatedComponentUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restComponentUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedComponentUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedComponentUnit))
            )
            .andExpect(status().isOk());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
        ComponentUnit testComponentUnit = componentUnitList.get(componentUnitList.size() - 1);
        assertThat(testComponentUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComponentUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComponentUnit.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, componentUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(componentUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(componentUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComponentUnitWithPatch() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();

        // Update the componentUnit using partial update
        ComponentUnit partialUpdatedComponentUnit = new ComponentUnit();
        partialUpdatedComponentUnit.setId(componentUnit.getId());

        partialUpdatedComponentUnit.description(UPDATED_DESCRIPTION);

        restComponentUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentUnit))
            )
            .andExpect(status().isOk());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
        ComponentUnit testComponentUnit = componentUnitList.get(componentUnitList.size() - 1);
        assertThat(testComponentUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testComponentUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComponentUnit.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateComponentUnitWithPatch() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();

        // Update the componentUnit using partial update
        ComponentUnit partialUpdatedComponentUnit = new ComponentUnit();
        partialUpdatedComponentUnit.setId(componentUnit.getId());

        partialUpdatedComponentUnit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restComponentUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComponentUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComponentUnit))
            )
            .andExpect(status().isOk());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
        ComponentUnit testComponentUnit = componentUnitList.get(componentUnitList.size() - 1);
        assertThat(testComponentUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testComponentUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComponentUnit.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, componentUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(componentUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComponentUnit() throws Exception {
        int databaseSizeBeforeUpdate = componentUnitRepository.findAll().size();
        componentUnit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComponentUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(componentUnit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComponentUnit in the database
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComponentUnit() throws Exception {
        // Initialize the database
        componentUnitRepository.saveAndFlush(componentUnit);

        int databaseSizeBeforeDelete = componentUnitRepository.findAll().size();

        // Delete the componentUnit
        restComponentUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, componentUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ComponentUnit> componentUnitList = componentUnitRepository.findAll();
        assertThat(componentUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
