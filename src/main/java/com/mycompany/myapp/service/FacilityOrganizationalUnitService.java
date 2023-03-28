package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FacilityOrganizationalUnit;
import com.mycompany.myapp.repository.FacilityOrganizationalUnitRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FacilityOrganizationalUnit}.
 */
@Service
@Transactional
public class FacilityOrganizationalUnitService {

    private final Logger log = LoggerFactory.getLogger(FacilityOrganizationalUnitService.class);

    private final FacilityOrganizationalUnitRepository facilityOrganizationalUnitRepository;

    public FacilityOrganizationalUnitService(FacilityOrganizationalUnitRepository facilityOrganizationalUnitRepository) {
        this.facilityOrganizationalUnitRepository = facilityOrganizationalUnitRepository;
    }

    /**
     * Save a facilityOrganizationalUnit.
     *
     * @param facilityOrganizationalUnit the entity to save.
     * @return the persisted entity.
     */
    public FacilityOrganizationalUnit save(FacilityOrganizationalUnit facilityOrganizationalUnit) {
        log.debug("Request to save FacilityOrganizationalUnit : {}", facilityOrganizationalUnit);
        return facilityOrganizationalUnitRepository.save(facilityOrganizationalUnit);
    }

    /**
     * Update a facilityOrganizationalUnit.
     *
     * @param facilityOrganizationalUnit the entity to save.
     * @return the persisted entity.
     */
    public FacilityOrganizationalUnit update(FacilityOrganizationalUnit facilityOrganizationalUnit) {
        log.debug("Request to update FacilityOrganizationalUnit : {}", facilityOrganizationalUnit);
        return facilityOrganizationalUnitRepository.save(facilityOrganizationalUnit);
    }

    /**
     * Partially update a facilityOrganizationalUnit.
     *
     * @param facilityOrganizationalUnit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FacilityOrganizationalUnit> partialUpdate(FacilityOrganizationalUnit facilityOrganizationalUnit) {
        log.debug("Request to partially update FacilityOrganizationalUnit : {}", facilityOrganizationalUnit);

        return facilityOrganizationalUnitRepository
            .findById(facilityOrganizationalUnit.getId())
            .map(existingFacilityOrganizationalUnit -> {
                if (facilityOrganizationalUnit.getName() != null) {
                    existingFacilityOrganizationalUnit.setName(facilityOrganizationalUnit.getName());
                }
                if (facilityOrganizationalUnit.getDescription() != null) {
                    existingFacilityOrganizationalUnit.setDescription(facilityOrganizationalUnit.getDescription());
                }
                if (facilityOrganizationalUnit.getCode() != null) {
                    existingFacilityOrganizationalUnit.setCode(facilityOrganizationalUnit.getCode());
                }

                return existingFacilityOrganizationalUnit;
            })
            .map(facilityOrganizationalUnitRepository::save);
    }

    /**
     * Get all the facilityOrganizationalUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FacilityOrganizationalUnit> findAll(Pageable pageable) {
        log.debug("Request to get all FacilityOrganizationalUnits");
        return facilityOrganizationalUnitRepository.findAll(pageable);
    }

    /**
     * Get one facilityOrganizationalUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FacilityOrganizationalUnit> findOne(Long id) {
        log.debug("Request to get FacilityOrganizationalUnit : {}", id);
        return facilityOrganizationalUnitRepository.findById(id);
    }

    /**
     * Delete the facilityOrganizationalUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FacilityOrganizationalUnit : {}", id);
        facilityOrganizationalUnitRepository.deleteById(id);
    }
}
