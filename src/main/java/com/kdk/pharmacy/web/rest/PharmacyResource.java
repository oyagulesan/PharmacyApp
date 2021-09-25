package com.kdk.pharmacy.web.rest;

import com.kdk.pharmacy.domain.Pharmacy;
import com.kdk.pharmacy.repository.PharmacyRepository;
import com.kdk.pharmacy.service.PharmacyService;
import com.kdk.pharmacy.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kdk.pharmacy.domain.Pharmacy}.
 */
@RestController
@RequestMapping("/api")
public class PharmacyResource {

    private final Logger log = LoggerFactory.getLogger(PharmacyResource.class);

    private static final String ENTITY_NAME = "pharmacy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PharmacyService pharmacyService;

    private final PharmacyRepository pharmacyRepository;

    public PharmacyResource(PharmacyService pharmacyService, PharmacyRepository pharmacyRepository) {
        this.pharmacyService = pharmacyService;
        this.pharmacyRepository = pharmacyRepository;
    }

    /**
     * {@code POST  /pharmacies} : Create a new pharmacy.
     *
     * @param pharmacy the pharmacy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pharmacy, or with status {@code 400 (Bad Request)} if the pharmacy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pharmacies")
    public ResponseEntity<Pharmacy> createPharmacy(@Valid @RequestBody Pharmacy pharmacy) throws URISyntaxException {
        log.debug("REST request to save Pharmacy : {}", pharmacy);
        if (pharmacy.getId() != null) {
            throw new BadRequestAlertException("A new pharmacy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pharmacy result = pharmacyService.save(pharmacy);
        return ResponseEntity
            .created(new URI("/api/pharmacies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pharmacies/:id} : Updates an existing pharmacy.
     *
     * @param id the id of the pharmacy to save.
     * @param pharmacy the pharmacy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pharmacy,
     * or with status {@code 400 (Bad Request)} if the pharmacy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pharmacy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pharmacies/{id}")
    public ResponseEntity<Pharmacy> updatePharmacy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pharmacy pharmacy
    ) throws URISyntaxException {
        log.debug("REST request to update Pharmacy : {}, {}", id, pharmacy);
        if (pharmacy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pharmacy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pharmacyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pharmacy result = pharmacyService.save(pharmacy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pharmacy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pharmacies/:id} : Partial updates given fields of an existing pharmacy, field will ignore if it is null
     *
     * @param id the id of the pharmacy to save.
     * @param pharmacy the pharmacy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pharmacy,
     * or with status {@code 400 (Bad Request)} if the pharmacy is not valid,
     * or with status {@code 404 (Not Found)} if the pharmacy is not found,
     * or with status {@code 500 (Internal Server Error)} if the pharmacy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pharmacies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pharmacy> partialUpdatePharmacy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pharmacy pharmacy
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pharmacy partially : {}, {}", id, pharmacy);
        if (pharmacy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pharmacy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pharmacyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pharmacy> result = pharmacyService.partialUpdate(pharmacy);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pharmacy.getId().toString())
        );
    }

    /**
     * {@code GET  /pharmacies} : get all the pharmacies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pharmacies in body.
     */
    @GetMapping("/pharmacies")
    public List<Pharmacy> getAllPharmacies() {
        log.debug("REST request to get all Pharmacies");
        return pharmacyService.findAll();
    }

    /**
     * {@code GET  /pharmacies/:id} : get the "id" pharmacy.
     *
     * @param id the id of the pharmacy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pharmacy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pharmacies/{id}")
    public ResponseEntity<Pharmacy> getPharmacy(@PathVariable Long id) {
        log.debug("REST request to get Pharmacy : {}", id);
        Optional<Pharmacy> pharmacy = pharmacyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pharmacy);
    }

    /**
     * {@code DELETE  /pharmacies/:id} : delete the "id" pharmacy.
     *
     * @param id the id of the pharmacy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pharmacies/{id}")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        log.debug("REST request to delete Pharmacy : {}", id);
        pharmacyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
