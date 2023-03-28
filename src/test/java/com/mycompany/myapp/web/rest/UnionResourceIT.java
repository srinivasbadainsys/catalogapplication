package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
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
 * Integration tests for the {@link UnionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnionRepository unionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnionMockMvc;

    private Union union;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Union createEntity(EntityManager em) {
        Union union = new Union().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return union;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Union createUpdatedEntity(EntityManager em) {
        Union union = new Union().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return union;
    }

    @BeforeEach
    public void initTest() {
        union = createEntity(em);
    }

    @Test
    @Transactional
    void createUnion() throws Exception {
        int databaseSizeBeforeCreate = unionRepository.findAll().size();
        // Create the Union
        restUnionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(union)))
            .andExpect(status().isCreated());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeCreate + 1);
        Union testUnion = unionList.get(unionList.size() - 1);
        assertThat(testUnion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUnion.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createUnionWithExistingId() throws Exception {
        // Create the Union with an existing ID
        union.setId(1L);

        int databaseSizeBeforeCreate = unionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(union)))
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUnions() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        // Get all the unionList
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(union.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getUnion() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        // Get the union
        restUnionMockMvc
            .perform(get(ENTITY_API_URL_ID, union.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(union.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingUnion() throws Exception {
        // Get the union
        restUnionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnion() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        int databaseSizeBeforeUpdate = unionRepository.findAll().size();

        // Update the union
        Union updatedUnion = unionRepository.findById(union.getId()).get();
        // Disconnect from session so that the updates on updatedUnion are not directly saved in db
        em.detach(updatedUnion);
        updatedUnion.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restUnionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
        Union testUnion = unionList.get(unionList.size() - 1);
        assertThat(testUnion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUnion.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, union.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(union)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnionWithPatch() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        int databaseSizeBeforeUpdate = unionRepository.findAll().size();

        // Update the union using partial update
        Union partialUpdatedUnion = new Union();
        partialUpdatedUnion.setId(union.getId());

        partialUpdatedUnion.description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
        Union testUnion = unionList.get(unionList.size() - 1);
        assertThat(testUnion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUnion.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateUnionWithPatch() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        int databaseSizeBeforeUpdate = unionRepository.findAll().size();

        // Update the union using partial update
        Union partialUpdatedUnion = new Union();
        partialUpdatedUnion.setId(union.getId());

        partialUpdatedUnion.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
        Union testUnion = unionList.get(unionList.size() - 1);
        assertThat(testUnion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUnion.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, union.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnion() throws Exception {
        int databaseSizeBeforeUpdate = unionRepository.findAll().size();
        union.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(union)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Union in the database
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnion() throws Exception {
        // Initialize the database
        unionRepository.saveAndFlush(union);

        int databaseSizeBeforeDelete = unionRepository.findAll().size();

        // Delete the union
        restUnionMockMvc
            .perform(delete(ENTITY_API_URL_ID, union.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Union> unionList = unionRepository.findAll();
        assertThat(unionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
