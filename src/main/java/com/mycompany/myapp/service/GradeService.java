package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Grade;
import com.mycompany.myapp.repository.GradeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Grade}.
 */
@Service
@Transactional
public class GradeService {

    private final Logger log = LoggerFactory.getLogger(GradeService.class);

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    /**
     * Save a grade.
     *
     * @param grade the entity to save.
     * @return the persisted entity.
     */
    public Grade save(Grade grade) {
        log.debug("Request to save Grade : {}", grade);
        return gradeRepository.save(grade);
    }

    /**
     * Update a grade.
     *
     * @param grade the entity to save.
     * @return the persisted entity.
     */
    public Grade update(Grade grade) {
        log.debug("Request to update Grade : {}", grade);
        return gradeRepository.save(grade);
    }

    /**
     * Partially update a grade.
     *
     * @param grade the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Grade> partialUpdate(Grade grade) {
        log.debug("Request to partially update Grade : {}", grade);

        return gradeRepository
            .findById(grade.getId())
            .map(existingGrade -> {
                if (grade.getName() != null) {
                    existingGrade.setName(grade.getName());
                }
                if (grade.getDescription() != null) {
                    existingGrade.setDescription(grade.getDescription());
                }
                if (grade.getCode() != null) {
                    existingGrade.setCode(grade.getCode());
                }

                return existingGrade;
            })
            .map(gradeRepository::save);
    }

    /**
     * Get all the grades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Grade> findAll(Pageable pageable) {
        log.debug("Request to get all Grades");
        return gradeRepository.findAll(pageable);
    }

    /**
     * Get one grade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Grade> findOne(Long id) {
        log.debug("Request to get Grade : {}", id);
        return gradeRepository.findById(id);
    }

    /**
     * Delete the grade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Grade : {}", id);
        gradeRepository.deleteById(id);
    }
}
