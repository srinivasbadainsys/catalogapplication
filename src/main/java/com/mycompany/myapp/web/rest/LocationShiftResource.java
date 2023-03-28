package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LocationShift;
import com.mycompany.myapp.repository.LocationShiftRepository;
import com.mycompany.myapp.service.LocationShiftService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LocationShift}.
 */
@RestController
@RequestMapping("/api")
public class LocationShiftResource {

    private final Logger log = LoggerFactory.getLogger(LocationShiftResource.class);

    private static final String ENTITY_NAME = "locationShift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationShiftService locationShiftService;

    private final LocationShiftRepository locationShiftRepository;

    public LocationShiftResource(LocationShiftService locationShiftService, LocationShiftRepository locationShiftRepository) {
        this.locationShiftService = locationShiftService;
        this.locationShiftRepository = locationShiftRepository;
    }

    /**
     * {@code POST  /location-shifts} : Create a new locationShift.
     *
     * @param locationShift the locationShift to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationShift, or with status {@code 400 (Bad Request)} if the locationShift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-shifts")
    public ResponseEntity<LocationShift> createLocationShift(@RequestBody LocationShift locationShift) throws URISyntaxException {
        log.debug("REST request to save LocationShift : {}", locationShift);
        if (locationShift.getId() != null) {
            throw new BadRequestAlertException("A new locationShift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationShift result = locationShiftService.save(locationShift);
        return ResponseEntity
            .created(new URI("/api/location-shifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-shifts/:id} : Updates an existing locationShift.
     *
     * @param id the id of the locationShift to save.
     * @param locationShift the locationShift to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationShift,
     * or with status {@code 400 (Bad Request)} if the locationShift is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationShift couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-shifts/{id}")
    public ResponseEntity<LocationShift> updateLocationShift(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationShift locationShift
    ) throws URISyntaxException {
        log.debug("REST request to update LocationShift : {}, {}", id, locationShift);
        if (locationShift.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationShift.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationShiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationShift result = locationShiftService.update(locationShift);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationShift.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-shifts/:id} : Partial updates given fields of an existing locationShift, field will ignore if it is null
     *
     * @param id the id of the locationShift to save.
     * @param locationShift the locationShift to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationShift,
     * or with status {@code 400 (Bad Request)} if the locationShift is not valid,
     * or with status {@code 404 (Not Found)} if the locationShift is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationShift couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-shifts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationShift> partialUpdateLocationShift(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationShift locationShift
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationShift partially : {}, {}", id, locationShift);
        if (locationShift.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationShift.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationShiftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationShift> result = locationShiftService.partialUpdate(locationShift);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationShift.getId().toString())
        );
    }

    /**
     * {@code GET  /location-shifts} : get all the locationShifts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationShifts in body.
     */
    @GetMapping("/location-shifts")
    public ResponseEntity<List<LocationShift>> getAllLocationShifts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LocationShifts");
        Page<LocationShift> page = locationShiftService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /location-shifts/:id} : get the "id" locationShift.
     *
     * @param id the id of the locationShift to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationShift, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-shifts/{id}")
    public ResponseEntity<LocationShift> getLocationShift(@PathVariable Long id) {
        log.debug("REST request to get LocationShift : {}", id);
        Optional<LocationShift> locationShift = locationShiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationShift);
    }

    /**
     * {@code DELETE  /location-shifts/:id} : delete the "id" locationShift.
     *
     * @param id the id of the locationShift to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-shifts/{id}")
    public ResponseEntity<Void> deleteLocationShift(@PathVariable Long id) {
        log.debug("REST request to delete LocationShift : {}", id);
        locationShiftService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
