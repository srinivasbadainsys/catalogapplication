package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PayType;
import com.mycompany.myapp.repository.PayTypeRepository;
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
 * Integration tests for the {@link PayTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PayTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pay-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayTypeRepository payTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPayTypeMockMvc;

    private PayType payType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayType createEntity(EntityManager em) {
        PayType payType = new PayType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE);
        return payType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayType createUpdatedEntity(EntityManager em) {
        PayType payType = new PayType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);
        return payType;
    }

    @BeforeEach
    public void initTest() {
        payType = createEntity(em);
    }

    @Test
    @Transactional
    void createPayType() throws Exception {
        int databaseSizeBeforeCreate = payTypeRepository.findAll().size();
        // Create the PayType
        restPayTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payType)))
            .andExpect(status().isCreated());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PayType testPayType = payTypeList.get(payTypeList.size() - 1);
        assertThat(testPayType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createPayTypeWithExistingId() throws Exception {
        // Create the PayType with an existing ID
        payType.setId(1L);

        int databaseSizeBeforeCreate = payTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payType)))
            .andExpect(status().isBadRequest());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPayTypes() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        // Get all the payTypeList
        restPayTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getPayType() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        // Get the payType
        restPayTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, payType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingPayType() throws Exception {
        // Get the payType
        restPayTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayType() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();

        // Update the payType
        PayType updatedPayType = payTypeRepository.findById(payType.getId()).get();
        // Disconnect from session so that the updates on updatedPayType are not directly saved in db
        em.detach(updatedPayType);
        updatedPayType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restPayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayType))
            )
            .andExpect(status().isOk());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
        PayType testPayType = payTypeList.get(payTypeList.size() - 1);
        assertThat(testPayType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePayTypeWithPatch() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();

        // Update the payType using partial update
        PayType partialUpdatedPayType = new PayType();
        partialUpdatedPayType.setId(payType.getId());

        partialUpdatedPayType.name(UPDATED_NAME).code(UPDATED_CODE);

        restPayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayType))
            )
            .andExpect(status().isOk());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
        PayType testPayType = payTypeList.get(payTypeList.size() - 1);
        assertThat(testPayType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdatePayTypeWithPatch() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();

        // Update the payType using partial update
        PayType partialUpdatedPayType = new PayType();
        partialUpdatedPayType.setId(payType.getId());

        partialUpdatedPayType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).code(UPDATED_CODE);

        restPayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayType))
            )
            .andExpect(status().isOk());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
        PayType testPayType = payTypeList.get(payTypeList.size() - 1);
        assertThat(testPayType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayType() throws Exception {
        int databaseSizeBeforeUpdate = payTypeRepository.findAll().size();
        payType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPayTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PayType in the database
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayType() throws Exception {
        // Initialize the database
        payTypeRepository.saveAndFlush(payType);

        int databaseSizeBeforeDelete = payTypeRepository.findAll().size();

        // Delete the payType
        restPayTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, payType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PayType> payTypeList = payTypeRepository.findAll();
        assertThat(payTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
