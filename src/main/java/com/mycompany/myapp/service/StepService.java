package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Step;
import com.mycompany.myapp.repository.StepRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Step}.
 */
@Service
@Transactional
public class StepService {

    private final Logger log = LoggerFactory.getLogger(StepService.class);

    private final StepRepository stepRepository;

    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    /**
     * Save a step.
     *
     * @param step the entity to save.
     * @return the persisted entity.
     */
    public Step save(Step step) {
        log.debug("Request to save Step : {}", step);
        return stepRepository.save(step);
    }

    /**
     * Update a step.
     *
     * @param step the entity to save.
     * @return the persisted entity.
     */
    public Step update(Step step) {
        log.debug("Request to update Step : {}", step);
        return stepRepository.save(step);
    }

    /**
     * Partially update a step.
     *
     * @param step the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Step> partialUpdate(Step step) {
        log.debug("Request to partially update Step : {}", step);

        return stepRepository
            .findById(step.getId())
            .map(existingStep -> {
                if (step.getName() != null) {
                    existingStep.setName(step.getName());
                }
                if (step.getDescription() != null) {
                    existingStep.setDescription(step.getDescription());
                }
                if (step.getCode() != null) {
                    existingStep.setCode(step.getCode());
                }

                return existingStep;
            })
            .map(stepRepository::save);
    }

    /**
     * Get all the steps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Step> findAll(Pageable pageable) {
        log.debug("Request to get all Steps");
        return stepRepository.findAll(pageable);
    }

    /**
     * Get one step by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Step> findOne(Long id) {
        log.debug("Request to get Step : {}", id);
        return stepRepository.findById(id);
    }

    /**
     * Delete the step by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Step : {}", id);
        stepRepository.deleteById(id);
    }
}
