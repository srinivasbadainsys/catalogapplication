package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FacilityOrganizationalUnit;
import com.mycompany.myapp.repository.FacilityOrganizationalUnitRepository;
import com.mycompany.myapp.service.FacilityOrganizationalUnitService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FacilityOrganizationalUnit}.
 */
@RestController
@RequestMapping("/api")
public class FacilityOrganizationalUnitResource {

    private final Logger log = LoggerFactory.getLogger(FacilityOrganizationalUnitResource.class);

    private static final String ENTITY_NAME = "facilityOrganizationalUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilityOrganizationalUnitService facilityOrganizationalUnitService;

    private final FacilityOrganizationalUnitRepository facilityOrganizationalUnitRepository;

    public FacilityOrganizationalUnitResource(
        FacilityOrganizationalUnitService facilityOrganizationalUnitService,
        FacilityOrganizationalUnitRepository facilityOrganizationalUnitRepository
    ) {
        this.facilityOrganizationalUnitService = facilityOrganizationalUnitService;
        this.facilityOrganizationalUnitRepository = facilityOrganizationalUnitRepository;
    }

    /**
     * {@code POST  /facility-organizational-units} : Create a new facilityOrganizationalUnit.
     *
     * @param facilityOrganizationalUnit the facilityOrganizationalUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilityOrganizationalUnit, or with status {@code 400 (Bad Request)} if the facilityOrganizationalUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facility-organizational-units")
    public ResponseEntity<FacilityOrganizationalUnit> createFacilityOrganizationalUnit(
        @RequestBody FacilityOrganizationalUnit facilityOrganizationalUnit
    ) throws URISyntaxException {
        log.debug("REST request to save FacilityOrganizationalUnit : {}", facilityOrganizationalUnit);
        if (facilityOrganizationalUnit.getId() != null) {
            throw new BadRequestAlertException("A new facilityOrganizationalUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacilityOrganizationalUnit result = facilityOrganizationalUnitService.save(facilityOrganizationalUnit);
        return ResponseEntity
            .created(new URI("/api/facility-organizational-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facility-organizational-units/:id} : Updates an existing facilityOrganizationalUnit.
     *
     * @param id the id of the facilityOrganizationalUnit to save.
     * @param facilityOrganizationalUnit the facilityOrganizationalUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityOrganizationalUnit,
     * or with status {@code 400 (Bad Request)} if the facilityOrganizationalUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilityOrganizationalUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facility-organizational-units/{id}")
    public ResponseEntity<FacilityOrganizationalUnit> updateFacilityOrganizationalUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacilityOrganizationalUnit facilityOrganizationalUnit
    ) throws URISyntaxException {
        log.debug("REST request to update FacilityOrganizationalUnit : {}, {}", id, facilityOrganizationalUnit);
        if (facilityOrganizationalUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityOrganizationalUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityOrganizationalUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacilityOrganizationalUnit result = facilityOrganizationalUnitService.update(facilityOrganizationalUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityOrganizationalUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facility-organizational-units/:id} : Partial updates given fields of an existing facilityOrganizationalUnit, field will ignore if it is null
     *
     * @param id the id of the facilityOrganizationalUnit to save.
     * @param facilityOrganizationalUnit the facilityOrganizationalUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilityOrganizationalUnit,
     * or with status {@code 400 (Bad Request)} if the facilityOrganizationalUnit is not valid,
     * or with status {@code 404 (Not Found)} if the facilityOrganizationalUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilityOrganizationalUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facility-organizational-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacilityOrganizationalUnit> partialUpdateFacilityOrganizationalUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacilityOrganizationalUnit facilityOrganizationalUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update FacilityOrganizationalUnit partially : {}, {}", id, facilityOrganizationalUnit);
        if (facilityOrganizationalUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilityOrganizationalUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilityOrganizationalUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacilityOrganizationalUnit> result = facilityOrganizationalUnitService.partialUpdate(facilityOrganizationalUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facilityOrganizationalUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /facility-organizational-units} : get all the facilityOrganizationalUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilityOrganizationalUnits in body.
     */
    @GetMapping("/facility-organizational-units")
    public ResponseEntity<List<FacilityOrganizationalUnit>> getAllFacilityOrganizationalUnits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FacilityOrganizationalUnits");
        Page<FacilityOrganizationalUnit> page = facilityOrganizationalUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facility-organizational-units/:id} : get the "id" facilityOrganizationalUnit.
     *
     * @param id the id of the facilityOrganizationalUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilityOrganizationalUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facility-organizational-units/{id}")
    public ResponseEntity<FacilityOrganizationalUnit> getFacilityOrganizationalUnit(@PathVariable Long id) {
        log.debug("REST request to get FacilityOrganizationalUnit : {}", id);
        Optional<FacilityOrganizationalUnit> facilityOrganizationalUnit = facilityOrganizationalUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facilityOrganizationalUnit);
    }

    /**
     * {@code DELETE  /facility-organizational-units/:id} : delete the "id" facilityOrganizationalUnit.
     *
     * @param id the id of the facilityOrganizationalUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facility-organizational-units/{id}")
    public ResponseEntity<Void> deleteFacilityOrganizationalUnit(@PathVariable Long id) {
        log.debug("REST request to delete FacilityOrganizationalUnit : {}", id);
        facilityOrganizationalUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
