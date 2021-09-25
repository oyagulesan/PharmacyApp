package com.kdk.pharmacy.service.impl;

import com.kdk.pharmacy.domain.Pharmacy;
import com.kdk.pharmacy.repository.PharmacyRepository;
import com.kdk.pharmacy.service.PharmacyService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pharmacy}.
 */
@Service
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final Logger log = LoggerFactory.getLogger(PharmacyServiceImpl.class);

    private final PharmacyRepository pharmacyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public Pharmacy save(Pharmacy pharmacy) {
        log.debug("Request to save Pharmacy : {}", pharmacy);
        return pharmacyRepository.save(pharmacy);
    }

    @Override
    public Optional<Pharmacy> partialUpdate(Pharmacy pharmacy) {
        log.debug("Request to partially update Pharmacy : {}", pharmacy);

        return pharmacyRepository
            .findById(pharmacy.getId())
            .map(existingPharmacy -> {
                if (pharmacy.getPharmacyName() != null) {
                    existingPharmacy.setPharmacyName(pharmacy.getPharmacyName());
                }

                return existingPharmacy;
            })
            .map(pharmacyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmacy> findAll() {
        log.debug("Request to get all Pharmacies");
        return pharmacyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pharmacy> findOne(Long id) {
        log.debug("Request to get Pharmacy : {}", id);
        return pharmacyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pharmacy : {}", id);
        pharmacyRepository.deleteById(id);
    }
}
