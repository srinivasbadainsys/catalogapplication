package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Union}.
 */
@Service
@Transactional
public class UnionService {

    private final Logger log = LoggerFactory.getLogger(UnionService.class);

    private final UnionRepository unionRepository;

    public UnionService(UnionRepository unionRepository) {
        this.unionRepository = unionRepository;
    }

    /**
     * Save a union.
     *
     * @param union the entity to save.
     * @return the persisted entity.
     */
    public Union save(Union union) {
        log.debug("Request to save Union : {}", union);
        return unionRepository.save(union);
    }

    /**
     * Update a union.
     *
     * @param union the entity to save.
     * @return the persisted entity.
     */
    public Union update(Union union) {
        log.debug("Request to update Union : {}", union);
        return unionRepository.save(union);
    }

    /**
     * Partially update a union.
     *
     * @param union the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Union> partialUpdate(Union union) {
        log.debug("Request to partially update Union : {}", union);

        return unionRepository
            .findById(union.getId())
            .map(existingUnion -> {
                if (union.getName() != null) {
                    existingUnion.setName(union.getName());
                }
                if (union.getDescription() != null) {
                    existingUnion.setDescription(union.getDescription());
                }
                if (union.getCode() != null) {
                    existingUnion.setCode(union.getCode());
                }

                return existingUnion;
            })
            .map(unionRepository::save);
    }

    /**
     * Get all the unions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Union> findAll(Pageable pageable) {
        log.debug("Request to get all Unions");
        return unionRepository.findAll(pageable);
    }

    /**
     * Get one union by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Union> findOne(Long id) {
        log.debug("Request to get Union : {}", id);
        return unionRepository.findById(id);
    }

    /**
     * Delete the union by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Union : {}", id);
        unionRepository.deleteById(id);
    }
}
