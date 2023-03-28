package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LocationShift;
import com.mycompany.myapp.repository.LocationShiftRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationShift}.
 */
@Service
@Transactional
public class LocationShiftService {

    private final Logger log = LoggerFactory.getLogger(LocationShiftService.class);

    private final LocationShiftRepository locationShiftRepository;

    public LocationShiftService(LocationShiftRepository locationShiftRepository) {
        this.locationShiftRepository = locationShiftRepository;
    }

    /**
     * Save a locationShift.
     *
     * @param locationShift the entity to save.
     * @return the persisted entity.
     */
    public LocationShift save(LocationShift locationShift) {
        log.debug("Request to save LocationShift : {}", locationShift);
        return locationShiftRepository.save(locationShift);
    }

    /**
     * Update a locationShift.
     *
     * @param locationShift the entity to save.
     * @return the persisted entity.
     */
    public LocationShift update(LocationShift locationShift) {
        log.debug("Request to update LocationShift : {}", locationShift);
        return locationShiftRepository.save(locationShift);
    }

    /**
     * Partially update a locationShift.
     *
     * @param locationShift the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationShift> partialUpdate(LocationShift locationShift) {
        log.debug("Request to partially update LocationShift : {}", locationShift);

        return locationShiftRepository
            .findById(locationShift.getId())
            .map(existingLocationShift -> {
                if (locationShift.getName() != null) {
                    existingLocationShift.setName(locationShift.getName());
                }
                if (locationShift.getDescription() != null) {
                    existingLocationShift.setDescription(locationShift.getDescription());
                }
                if (locationShift.getCode() != null) {
                    existingLocationShift.setCode(locationShift.getCode());
                }

                return existingLocationShift;
            })
            .map(locationShiftRepository::save);
    }

    /**
     * Get all the locationShifts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationShift> findAll(Pageable pageable) {
        log.debug("Request to get all LocationShifts");
        return locationShiftRepository.findAll(pageable);
    }

    /**
     * Get one locationShift by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationShift> findOne(Long id) {
        log.debug("Request to get LocationShift : {}", id);
        return locationShiftRepository.findById(id);
    }

    /**
     * Delete the locationShift by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationShift : {}", id);
        locationShiftRepository.deleteById(id);
    }
}
