package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmploymentStatus;
import com.mycompany.myapp.repository.EmploymentStatusRepository;
import com.mycompany.myapp.service.EmploymentStatusService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EmploymentStatus}.
 */
@RestController
@RequestMapping("/api")
public class EmploymentStatusResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentStatusResource.class);

    private static final String ENTITY_NAME = "employmentStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentStatusService employmentStatusService;

    private final EmploymentStatusRepository employmentStatusRepository;

    public EmploymentStatusResource(
        EmploymentStatusService employmentStatusService,
        EmploymentStatusRepository employmentStatusRepository
    ) {
        this.employmentStatusService = employmentStatusService;
        this.employmentStatusRepository = employmentStatusRepository;
    }

    /**
     * {@code POST  /employment-statuses} : Create a new employmentStatus.
     *
     * @param employmentStatus the employmentStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentStatus, or with status {@code 400 (Bad Request)} if the employmentStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employment-statuses")
    public ResponseEntity<EmploymentStatus> createEmploymentStatus(@RequestBody EmploymentStatus employmentStatus)
        throws URISyntaxException {
        log.debug("REST request to save EmploymentStatus : {}", employmentStatus);
        if (employmentStatus.getId() != null) {
            throw new BadRequestAlertException("A new employmentStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmploymentStatus result = employmentStatusService.save(employmentStatus);
        return ResponseEntity
            .created(new URI("/api/employment-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employment-statuses/:id} : Updates an existing employmentStatus.
     *
     * @param id the id of the employmentStatus to save.
     * @param employmentStatus the employmentStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentStatus,
     * or with status {@code 400 (Bad Request)} if the employmentStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employment-statuses/{id}")
    public ResponseEntity<EmploymentStatus> updateEmploymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmploymentStatus employmentStatus
    ) throws URISyntaxException {
        log.debug("REST request to update EmploymentStatus : {}, {}", id, employmentStatus);
        if (employmentStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmploymentStatus result = employmentStatusService.update(employmentStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employment-statuses/:id} : Partial updates given fields of an existing employmentStatus, field will ignore if it is null
     *
     * @param id the id of the employmentStatus to save.
     * @param employmentStatus the employmentStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentStatus,
     * or with status {@code 400 (Bad Request)} if the employmentStatus is not valid,
     * or with status {@code 404 (Not Found)} if the employmentStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the employmentStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employment-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmploymentStatus> partialUpdateEmploymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmploymentStatus employmentStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmploymentStatus partially : {}, {}", id, employmentStatus);
        if (employmentStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmploymentStatus> result = employmentStatusService.partialUpdate(employmentStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /employment-statuses} : get all the employmentStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employmentStatuses in body.
     */
    @GetMapping("/employment-statuses")
    public ResponseEntity<List<EmploymentStatus>> getAllEmploymentStatuses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmploymentStatuses");
        Page<EmploymentStatus> page = employmentStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-statuses/:id} : get the "id" employmentStatus.
     *
     * @param id the id of the employmentStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employment-statuses/{id}")
    public ResponseEntity<EmploymentStatus> getEmploymentStatus(@PathVariable Long id) {
        log.debug("REST request to get EmploymentStatus : {}", id);
        Optional<EmploymentStatus> employmentStatus = employmentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentStatus);
    }

    /**
     * {@code DELETE  /employment-statuses/:id} : delete the "id" employmentStatus.
     *
     * @param id the id of the employmentStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employment-statuses/{id}")
    public ResponseEntity<Void> deleteEmploymentStatus(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentStatus : {}", id);
        employmentStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
