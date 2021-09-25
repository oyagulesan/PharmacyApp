package com.kdk.pharmacy.service;

import com.kdk.pharmacy.domain.Pharmacy;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Pharmacy}.
 */
public interface PharmacyService {
    /**
     * Save a pharmacy.
     *
     * @param pharmacy the entity to save.
     * @return the persisted entity.
     */
    Pharmacy save(Pharmacy pharmacy);

    /**
     * Partially updates a pharmacy.
     *
     * @param pharmacy the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pharmacy> partialUpdate(Pharmacy pharmacy);

    /**
     * Get all the pharmacies.
     *
     * @return the list of entities.
     */
    List<Pharmacy> findAll();

    /**
     * Get the "id" pharmacy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pharmacy> findOne(Long id);

    /**
     * Delete the "id" pharmacy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
