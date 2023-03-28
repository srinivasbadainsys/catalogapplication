package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EmploymentStatus;
import com.mycompany.myapp.repository.EmploymentStatusRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmploymentStatus}.
 */
@Service
@Transactional
public class EmploymentStatusService {

    private final Logger log = LoggerFactory.getLogger(EmploymentStatusService.class);

    private final EmploymentStatusRepository employmentStatusRepository;

    public EmploymentStatusService(EmploymentStatusRepository employmentStatusRepository) {
        this.employmentStatusRepository = employmentStatusRepository;
    }

    /**
     * Save a employmentStatus.
     *
     * @param employmentStatus the entity to save.
     * @return the persisted entity.
     */
    public EmploymentStatus save(EmploymentStatus employmentStatus) {
        log.debug("Request to save EmploymentStatus : {}", employmentStatus);
        return employmentStatusRepository.save(employmentStatus);
    }

    /**
     * Update a employmentStatus.
     *
     * @param employmentStatus the entity to save.
     * @return the persisted entity.
     */
    public EmploymentStatus update(EmploymentStatus employmentStatus) {
        log.debug("Request to update EmploymentStatus : {}", employmentStatus);
        return employmentStatusRepository.save(employmentStatus);
    }

    /**
     * Partially update a employmentStatus.
     *
     * @param employmentStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmploymentStatus> partialUpdate(EmploymentStatus employmentStatus) {
        log.debug("Request to partially update EmploymentStatus : {}", employmentStatus);

        return employmentStatusRepository
            .findById(employmentStatus.getId())
            .map(existingEmploymentStatus -> {
                if (employmentStatus.getName() != null) {
                    existingEmploymentStatus.setName(employmentStatus.getName());
                }
                if (employmentStatus.getDescription() != null) {
                    existingEmploymentStatus.setDescription(employmentStatus.getDescription());
                }
                if (employmentStatus.getCode() != null) {
                    existingEmploymentStatus.setCode(employmentStatus.getCode());
                }

                return existingEmploymentStatus;
            })
            .map(employmentStatusRepository::save);
    }

    /**
     * Get all the employmentStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmploymentStatus> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentStatuses");
        return employmentStatusRepository.findAll(pageable);
    }

    /**
     * Get one employmentStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmploymentStatus> findOne(Long id) {
        log.debug("Request to get EmploymentStatus : {}", id);
        return employmentStatusRepository.findById(id);
    }

    /**
     * Delete the employmentStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EmploymentStatus : {}", id);
        employmentStatusRepository.deleteById(id);
    }
}
