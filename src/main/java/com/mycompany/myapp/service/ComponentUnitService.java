package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ComponentUnit;
import com.mycompany.myapp.repository.ComponentUnitRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ComponentUnit}.
 */
@Service
@Transactional
public class ComponentUnitService {

    private final Logger log = LoggerFactory.getLogger(ComponentUnitService.class);

    private final ComponentUnitRepository componentUnitRepository;

    public ComponentUnitService(ComponentUnitRepository componentUnitRepository) {
        this.componentUnitRepository = componentUnitRepository;
    }

    /**
     * Save a componentUnit.
     *
     * @param componentUnit the entity to save.
     * @return the persisted entity.
     */
    public ComponentUnit save(ComponentUnit componentUnit) {
        log.debug("Request to save ComponentUnit : {}", componentUnit);
        return componentUnitRepository.save(componentUnit);
    }

    /**
     * Update a componentUnit.
     *
     * @param componentUnit the entity to save.
     * @return the persisted entity.
     */
    public ComponentUnit update(ComponentUnit componentUnit) {
        log.debug("Request to update ComponentUnit : {}", componentUnit);
        return componentUnitRepository.save(componentUnit);
    }

    /**
     * Partially update a componentUnit.
     *
     * @param componentUnit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComponentUnit> partialUpdate(ComponentUnit componentUnit) {
        log.debug("Request to partially update ComponentUnit : {}", componentUnit);

        return componentUnitRepository
            .findById(componentUnit.getId())
            .map(existingComponentUnit -> {
                if (componentUnit.getName() != null) {
                    existingComponentUnit.setName(componentUnit.getName());
                }
                if (componentUnit.getDescription() != null) {
                    existingComponentUnit.setDescription(componentUnit.getDescription());
                }
                if (componentUnit.getCode() != null) {
                    existingComponentUnit.setCode(componentUnit.getCode());
                }

                return existingComponentUnit;
            })
            .map(componentUnitRepository::save);
    }

    /**
     * Get all the componentUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComponentUnit> findAll(Pageable pageable) {
        log.debug("Request to get all ComponentUnits");
        return componentUnitRepository.findAll(pageable);
    }

    /**
     * Get one componentUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComponentUnit> findOne(Long id) {
        log.debug("Request to get ComponentUnit : {}", id);
        return componentUnitRepository.findById(id);
    }

    /**
     * Delete the componentUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ComponentUnit : {}", id);
        componentUnitRepository.deleteById(id);
    }
}
