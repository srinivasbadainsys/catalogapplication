package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PayLocality;
import com.mycompany.myapp.repository.PayLocalityRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PayLocality}.
 */
@Service
@Transactional
public class PayLocalityService {

    private final Logger log = LoggerFactory.getLogger(PayLocalityService.class);

    private final PayLocalityRepository payLocalityRepository;

    public PayLocalityService(PayLocalityRepository payLocalityRepository) {
        this.payLocalityRepository = payLocalityRepository;
    }

    /**
     * Save a payLocality.
     *
     * @param payLocality the entity to save.
     * @return the persisted entity.
     */
    public PayLocality save(PayLocality payLocality) {
        log.debug("Request to save PayLocality : {}", payLocality);
        return payLocalityRepository.save(payLocality);
    }

    /**
     * Update a payLocality.
     *
     * @param payLocality the entity to save.
     * @return the persisted entity.
     */
    public PayLocality update(PayLocality payLocality) {
        log.debug("Request to update PayLocality : {}", payLocality);
        return payLocalityRepository.save(payLocality);
    }

    /**
     * Partially update a payLocality.
     *
     * @param payLocality the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PayLocality> partialUpdate(PayLocality payLocality) {
        log.debug("Request to partially update PayLocality : {}", payLocality);

        return payLocalityRepository
            .findById(payLocality.getId())
            .map(existingPayLocality -> {
                if (payLocality.getName() != null) {
                    existingPayLocality.setName(payLocality.getName());
                }
                if (payLocality.getDescription() != null) {
                    existingPayLocality.setDescription(payLocality.getDescription());
                }
                if (payLocality.getCode() != null) {
                    existingPayLocality.setCode(payLocality.getCode());
                }

                return existingPayLocality;
            })
            .map(payLocalityRepository::save);
    }

    /**
     * Get all the payLocalities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PayLocality> findAll(Pageable pageable) {
        log.debug("Request to get all PayLocalities");
        return payLocalityRepository.findAll(pageable);
    }

    /**
     * Get one payLocality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PayLocality> findOne(Long id) {
        log.debug("Request to get PayLocality : {}", id);
        return payLocalityRepository.findById(id);
    }

    /**
     * Delete the payLocality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PayLocality : {}", id);
        payLocalityRepository.deleteById(id);
    }
}
