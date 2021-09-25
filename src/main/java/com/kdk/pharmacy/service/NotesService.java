package com.kdk.pharmacy.service;

import com.kdk.pharmacy.domain.Notes;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Notes}.
 */
public interface NotesService {
    /**
     * Save a notes.
     *
     * @param notes the entity to save.
     * @return the persisted entity.
     */
    Notes save(Notes notes);

    /**
     * Partially updates a notes.
     *
     * @param notes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Notes> partialUpdate(Notes notes);

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    List<Notes> findAll();

    /**
     * Get the "id" notes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Notes> findOne(Long id);

    /**
     * Delete the "id" notes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
