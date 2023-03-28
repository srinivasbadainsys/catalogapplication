package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CompPlanPayrollType;
import com.mycompany.myapp.repository.CompPlanPayrollTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompPlanPayrollType}.
 */
@Service
@Transactional
public class CompPlanPayrollTypeService {

    private final Logger log = LoggerFactory.getLogger(CompPlanPayrollTypeService.class);

    private final CompPlanPayrollTypeRepository compPlanPayrollTypeRepository;

    public CompPlanPayrollTypeService(CompPlanPayrollTypeRepository compPlanPayrollTypeRepository) {
        this.compPlanPayrollTypeRepository = compPlanPayrollTypeRepository;
    }

    /**
     * Save a compPlanPayrollType.
     *
     * @param compPlanPayrollType the entity to save.
     * @return the persisted entity.
     */
    public CompPlanPayrollType save(CompPlanPayrollType compPlanPayrollType) {
        log.debug("Request to save CompPlanPayrollType : {}", compPlanPayrollType);
        return compPlanPayrollTypeRepository.save(compPlanPayrollType);
    }

    /**
     * Update a compPlanPayrollType.
     *
     * @param compPlanPayrollType the entity to save.
     * @return the persisted entity.
     */
    public CompPlanPayrollType update(CompPlanPayrollType compPlanPayrollType) {
        log.debug("Request to update CompPlanPayrollType : {}", compPlanPayrollType);
        return compPlanPayrollTypeRepository.save(compPlanPayrollType);
    }

    /**
     * Partially update a compPlanPayrollType.
     *
     * @param compPlanPayrollType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompPlanPayrollType> partialUpdate(CompPlanPayrollType compPlanPayrollType) {
        log.debug("Request to partially update CompPlanPayrollType : {}", compPlanPayrollType);

        return compPlanPayrollTypeRepository
            .findById(compPlanPayrollType.getId())
            .map(existingCompPlanPayrollType -> {
                if (compPlanPayrollType.getName() != null) {
                    existingCompPlanPayrollType.setName(compPlanPayrollType.getName());
                }
                if (compPlanPayrollType.getDescription() != null) {
                    existingCompPlanPayrollType.setDescription(compPlanPayrollType.getDescription());
                }
                if (compPlanPayrollType.getCode() != null) {
                    existingCompPlanPayrollType.setCode(compPlanPayrollType.getCode());
                }

                return existingCompPlanPayrollType;
            })
            .map(compPlanPayrollTypeRepository::save);
    }

    /**
     * Get all the compPlanPayrollTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompPlanPayrollType> findAll(Pageable pageable) {
        log.debug("Request to get all CompPlanPayrollTypes");
        return compPlanPayrollTypeRepository.findAll(pageable);
    }

    /**
     * Get one compPlanPayrollType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompPlanPayrollType> findOne(Long id) {
        log.debug("Request to get CompPlanPayrollType : {}", id);
        return compPlanPayrollTypeRepository.findById(id);
    }

    /**
     * Delete the compPlanPayrollType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompPlanPayrollType : {}", id);
        compPlanPayrollTypeRepository.deleteById(id);
    }
}
