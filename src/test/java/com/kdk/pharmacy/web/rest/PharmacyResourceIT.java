package com.kdk.pharmacy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kdk.pharmacy.IntegrationTest;
import com.kdk.pharmacy.domain.Pharmacy;
import com.kdk.pharmacy.repository.PharmacyRepository;
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
 * Integration tests for the {@link PharmacyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PharmacyResourceIT {

    private static final String DEFAULT_PHARMACY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PHARMACY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pharmacies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPharmacyMockMvc;

    private Pharmacy pharmacy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pharmacy createEntity(EntityManager em) {
        Pharmacy pharmacy = new Pharmacy().pharmacyName(DEFAULT_PHARMACY_NAME);
        return pharmacy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pharmacy createUpdatedEntity(EntityManager em) {
        Pharmacy pharmacy = new Pharmacy().pharmacyName(UPDATED_PHARMACY_NAME);
        return pharmacy;
    }

    @BeforeEach
    public void initTest() {
        pharmacy = createEntity(em);
    }

    @Test
    @Transactional
    void createPharmacy() throws Exception {
        int databaseSizeBeforeCreate = pharmacyRepository.findAll().size();
        // Create the Pharmacy
        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacy)))
            .andExpect(status().isCreated());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeCreate + 1);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getPharmacyName()).isEqualTo(DEFAULT_PHARMACY_NAME);
    }

    @Test
    @Transactional
    void createPharmacyWithExistingId() throws Exception {
        // Create the Pharmacy with an existing ID
        pharmacy.setId(1L);

        int databaseSizeBeforeCreate = pharmacyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacy)))
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPharmacyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pharmacyRepository.findAll().size();
        // set the field null
        pharmacy.setPharmacyName(null);

        // Create the Pharmacy, which fails.

        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacy)))
            .andExpect(status().isBadRequest());

        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPharmacies() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        // Get all the pharmacyList
        restPharmacyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pharmacy.getId().intValue())))
            .andExpect(jsonPath("$.[*].pharmacyName").value(hasItem(DEFAULT_PHARMACY_NAME)));
    }

    @Test
    @Transactional
    void getPharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        // Get the pharmacy
        restPharmacyMockMvc
            .perform(get(ENTITY_API_URL_ID, pharmacy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pharmacy.getId().intValue()))
            .andExpect(jsonPath("$.pharmacyName").value(DEFAULT_PHARMACY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPharmacy() throws Exception {
        // Get the pharmacy
        restPharmacyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy
        Pharmacy updatedPharmacy = pharmacyRepository.findById(pharmacy.getId()).get();
        // Disconnect from session so that the updates on updatedPharmacy are not directly saved in db
        em.detach(updatedPharmacy);
        updatedPharmacy.pharmacyName(UPDATED_PHARMACY_NAME);

        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPharmacy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPharmacy))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getPharmacyName()).isEqualTo(UPDATED_PHARMACY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pharmacy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pharmacy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pharmacy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePharmacyWithPatch() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy using partial update
        Pharmacy partialUpdatedPharmacy = new Pharmacy();
        partialUpdatedPharmacy.setId(pharmacy.getId());

        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPharmacy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPharmacy))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getPharmacyName()).isEqualTo(DEFAULT_PHARMACY_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePharmacyWithPatch() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy using partial update
        Pharmacy partialUpdatedPharmacy = new Pharmacy();
        partialUpdatedPharmacy.setId(pharmacy.getId());

        partialUpdatedPharmacy.pharmacyName(UPDATED_PHARMACY_NAME);

        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPharmacy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPharmacy))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getPharmacyName()).isEqualTo(UPDATED_PHARMACY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pharmacy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pharmacy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pharmacy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pharmacy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeDelete = pharmacyRepository.findAll().size();

        // Delete the pharmacy
        restPharmacyMockMvc
            .perform(delete(ENTITY_API_URL_ID, pharmacy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
