package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PayType;
import com.mycompany.myapp.repository.PayTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PayType}.
 */
@Service
@Transactional
public class PayTypeService {

    private final Logger log = LoggerFactory.getLogger(PayTypeService.class);

    private final PayTypeRepository payTypeRepository;

    public PayTypeService(PayTypeRepository payTypeRepository) {
        this.payTypeRepository = payTypeRepository;
    }

    /**
     * Save a payType.
     *
     * @param payType the entity to save.
     * @return the persisted entity.
     */
    public PayType save(PayType payType) {
        log.debug("Request to save PayType : {}", payType);
        return payTypeRepository.save(payType);
    }

    /**
     * Update a payType.
     *
     * @param payType the entity to save.
     * @return the persisted entity.
     */
    public PayType update(PayType payType) {
        log.debug("Request to update PayType : {}", payType);
        return payTypeRepository.save(payType);
    }

    /**
     * Partially update a payType.
     *
     * @param payType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PayType> partialUpdate(PayType payType) {
        log.debug("Request to partially update PayType : {}", payType);

        return payTypeRepository
            .findById(payType.getId())
            .map(existingPayType -> {
                if (payType.getName() != null) {
                    existingPayType.setName(payType.getName());
                }
                if (payType.getDescription() != null) {
                    existingPayType.setDescription(payType.getDescription());
                }
                if (payType.getCode() != null) {
                    existingPayType.setCode(payType.getCode());
                }

                return existingPayType;
            })
            .map(payTypeRepository::save);
    }

    /**
     * Get all the payTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PayType> findAll(Pageable pageable) {
        log.debug("Request to get all PayTypes");
        return payTypeRepository.findAll(pageable);
    }

    /**
     * Get one payType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PayType> findOne(Long id) {
        log.debug("Request to get PayType : {}", id);
        return payTypeRepository.findById(id);
    }

    /**
     * Delete the payType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PayType : {}", id);
        payTypeRepository.deleteById(id);
    }
}
