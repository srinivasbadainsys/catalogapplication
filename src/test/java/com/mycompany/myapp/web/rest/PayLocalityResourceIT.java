package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PayLocality;
import com.mycompany.myapp.repository.PayLocalityRepository;
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
 * Integration tests for the {@link PayLocalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayLocalityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pay-localities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayLocalityRepository payLocalityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayLocalityMockMvc;

    private PayLocality payLocality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayLocality createEntity(EntityManager em) {
        PayLocality payLocality = new PayLocality().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return payLocality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayLocality createUpdatedEntity(EntityManager em) {
        PayLocality payLocality = new PayLocality().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return payLocality;
    }

    @BeforeEach
    public void initTest() {
        payLocality = createEntity(em);
    }

    @Test
    @Transactional
    void createPayLocality() throws Exception {
        int databaseSizeBeforeCreate = payLocalityRepository.findAll().size();
        // Create the PayLocality
        restPayLocalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payLocality)))
            .andExpect(status().isCreated());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeCreate + 1);
        PayLocality testPayLocality = payLocalityList.get(payLocalityList.size() - 1);
        assertThat(testPayLocality.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayLocality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayLocality.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createPayLocalityWithExistingId() throws Exception {
        // Create the PayLocality with an existing ID
        payLocality.setId(1L);

        int databaseSizeBeforeCreate = payLocalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayLocalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payLocality)))
            .andExpect(status().isBadRequest());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPayLocalities() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        // Get all the payLocalityList
        restPayLocalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payLocality.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getPayLocality() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        // Get the payLocality
        restPayLocalityMockMvc
            .perform(get(ENTITY_API_URL_ID, payLocality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payLocality.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingPayLocality() throws Exception {
        // Get the payLocality
        restPayLocalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayLocality() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();

        // Update the payLocality
        PayLocality updatedPayLocality = payLocalityRepository.findById(payLocality.getId()).get();
        // Disconnect from session so that the updates on updatedPayLocality are not directly saved in db
        em.detach(updatedPayLocality);
        updatedPayLocality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restPayLocalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayLocality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayLocality))
            )
            .andExpect(status().isOk());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
        PayLocality testPayLocality = payLocalityList.get(payLocalityList.size() - 1);
        assertThat(testPayLocality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayLocality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayLocality.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payLocality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payLocality))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payLocality))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payLocality)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayLocalityWithPatch() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();

        // Update the payLocality using partial update
        PayLocality partialUpdatedPayLocality = new PayLocality();
        partialUpdatedPayLocality.setId(payLocality.getId());

        partialUpdatedPayLocality.name(UPDATED_NAME);

        restPayLocalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayLocality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayLocality))
            )
            .andExpect(status().isOk());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
        PayLocality testPayLocality = payLocalityList.get(payLocalityList.size() - 1);
        assertThat(testPayLocality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayLocality.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayLocality.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdatePayLocalityWithPatch() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();

        // Update the payLocality using partial update
        PayLocality partialUpdatedPayLocality = new PayLocality();
        partialUpdatedPayLocality.setId(payLocality.getId());

        partialUpdatedPayLocality.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restPayLocalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayLocality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayLocality))
            )
            .andExpect(status().isOk());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
        PayLocality testPayLocality = payLocalityList.get(payLocalityList.size() - 1);
        assertThat(testPayLocality.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayLocality.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayLocality.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payLocality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payLocality))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payLocality))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayLocality() throws Exception {
        int databaseSizeBeforeUpdate = payLocalityRepository.findAll().size();
        payLocality.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayLocalityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payLocality))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayLocality in the database
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayLocality() throws Exception {
        // Initialize the database
        payLocalityRepository.saveAndFlush(payLocality);

        int databaseSizeBeforeDelete = payLocalityRepository.findAll().size();

        // Delete the payLocality
        restPayLocalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, payLocality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayLocality> payLocalityList = payLocalityRepository.findAll();
        assertThat(payLocalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
