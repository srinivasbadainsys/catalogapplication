package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmploymentType;
import com.mycompany.myapp.repository.EmploymentTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmploymentType}.
 */
@Service
@Transactional
public class EmploymentTypeService {

    private final Logger log = LoggerFactory.getLogger(EmploymentTypeService.class);

    private final EmploymentTypeRepository employmentTypeRepository;

    public EmploymentTypeService(EmploymentTypeRepository employmentTypeRepository) {
        this.employmentTypeRepository = employmentTypeRepository;
    }

    /**
     * Save a employmentType.
     *
     * @param employmentType the entity to save.
     * @return the persisted entity.
     */
    public EmploymentType save(EmploymentType employmentType) {
        log.debug("Request to save EmploymentType : {}", employmentType);
        return employmentTypeRepository.save(employmentType);
    }

    /**
     * Update a employmentType.
     *
     * @param employmentType the entity to save.
     * @return the persisted entity.
     */
    public EmploymentType update(EmploymentType employmentType) {
        log.debug("Request to update EmploymentType : {}", employmentType);
        return employmentTypeRepository.save(employmentType);
    }

    /**
     * Partially update a employmentType.
     *
     * @param employmentType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmploymentType> partialUpdate(EmploymentType employmentType) {
        log.debug("Request to partially update EmploymentType : {}", employmentType);

        return employmentTypeRepository
            .findById(employmentType.getId())
            .map(existingEmploymentType -> {
                if (employmentType.getName() != null) {
                    existingEmploymentType.setName(employmentType.getName());
                }
                if (employmentType.getDescription() != null) {
                    existingEmploymentType.setDescription(employmentType.getDescription());
                }
                if (employmentType.getCode() != null) {
                    existingEmploymentType.setCode(employmentType.getCode());
                }

                return existingEmploymentType;
            })
            .map(employmentTypeRepository::save);
    }

    /**
     * Get all the employmentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmploymentType> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentTypes");
        return employmentTypeRepository.findAll(pageable);
    }

    /**
     * Get one employmentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmploymentType> findOne(Long id) {
        log.debug("Request to get EmploymentType : {}", id);
        return employmentTypeRepository.findById(id);
    }

    /**
     * Delete the employmentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmploymentType : {}", id);
        employmentTypeRepository.deleteById(id);
    }
}
