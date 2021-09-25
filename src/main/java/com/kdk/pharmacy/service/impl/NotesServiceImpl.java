package com.kdk.pharmacy.service.impl;

import com.kdk.pharmacy.domain.Notes;
import com.kdk.pharmacy.repository.NotesRepository;
import com.kdk.pharmacy.service.NotesService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notes}.
 */
@Service
@Transactional
public class NotesServiceImpl implements NotesService {

    private final Logger log = LoggerFactory.getLogger(NotesServiceImpl.class);

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public Notes save(Notes notes) {
        log.debug("Request to save Notes : {}", notes);
        return notesRepository.save(notes);
    }

    @Override
    public Optional<Notes> partialUpdate(Notes notes) {
        log.debug("Request to partially update Notes : {}", notes);

        return notesRepository
            .findById(notes.getId())
            .map(existingNotes -> {
                if (notes.getDate() != null) {
                    existingNotes.setDate(notes.getDate());
                }
                if (notes.getComment() != null) {
                    existingNotes.setComment(notes.getComment());
                }

                return existingNotes;
            })
            .map(notesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notes> findAll() {
        log.debug("Request to get all Notes");
        return notesRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notes> findOne(Long id) {
        log.debug("Request to get Notes : {}", id);
        return notesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notes : {}", id);
        notesRepository.deleteById(id);
    }
}
