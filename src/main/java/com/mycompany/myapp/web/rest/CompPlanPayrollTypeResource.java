package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CompPlanPayrollType;
import com.mycompany.myapp.repository.CompPlanPayrollTypeRepository;
import com.mycompany.myapp.service.CompPlanPayrollTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CompPlanPayrollType}.
 */
@RestController
@RequestMapping("/api")
public class CompPlanPayrollTypeResource {

    private final Logger log = LoggerFactory.getLogger(CompPlanPayrollTypeResource.class);

    private static final String ENTITY_NAME = "compPlanPayrollType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompPlanPayrollTypeService compPlanPayrollTypeService;

    private final CompPlanPayrollTypeRepository compPlanPayrollTypeRepository;

    public CompPlanPayrollTypeResource(
        CompPlanPayrollTypeService compPlanPayrollTypeService,
        CompPlanPayrollTypeRepository compPlanPayrollTypeRepository
    ) {
        this.compPlanPayrollTypeService = compPlanPayrollTypeService;
        this.compPlanPayrollTypeRepository = compPlanPayrollTypeRepository;
    }

    /**
     * {@code POST  /comp-plan-payroll-types} : Create a new compPlanPayrollType.
     *
     * @param compPlanPayrollType the compPlanPayrollType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compPlanPayrollType, or with status {@code 400 (Bad Request)} if the compPlanPayrollType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comp-plan-payroll-types")
    public ResponseEntity<CompPlanPayrollType> createCompPlanPayrollType(@RequestBody CompPlanPayrollType compPlanPayrollType)
        throws URISyntaxException {
        log.debug("REST request to save CompPlanPayrollType : {}", compPlanPayrollType);
        if (compPlanPayrollType.getId() != null) {
            throw new BadRequestAlertException("A new compPlanPayrollType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompPlanPayrollType result = compPlanPayrollTypeService.save(compPlanPayrollType);
        return ResponseEntity
            .created(new URI("/api/comp-plan-payroll-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comp-plan-payroll-types/:id} : Updates an existing compPlanPayrollType.
     *
     * @param id the id of the compPlanPayrollType to save.
     * @param compPlanPayrollType the compPlanPayrollType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compPlanPayrollType,
     * or with status {@code 400 (Bad Request)} if the compPlanPayrollType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compPlanPayrollType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comp-plan-payroll-types/{id}")
    public ResponseEntity<CompPlanPayrollType> updateCompPlanPayrollType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompPlanPayrollType compPlanPayrollType
    ) throws URISyntaxException {
        log.debug("REST request to update CompPlanPayrollType : {}, {}", id, compPlanPayrollType);
        if (compPlanPayrollType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compPlanPayrollType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compPlanPayrollTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompPlanPayrollType result = compPlanPayrollTypeService.update(compPlanPayrollType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compPlanPayrollType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comp-plan-payroll-types/:id} : Partial updates given fields of an existing compPlanPayrollType, field will ignore if it is null
     *
     * @param id the id of the compPlanPayrollType to save.
     * @param compPlanPayrollType the compPlanPayrollType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compPlanPayrollType,
     * or with status {@code 400 (Bad Request)} if the compPlanPayrollType is not valid,
     * or with status {@code 404 (Not Found)} if the compPlanPayrollType is not found,
     * or with status {@code 500 (Internal Server Error)} if the compPlanPayrollType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comp-plan-payroll-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompPlanPayrollType> partialUpdateCompPlanPayrollType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompPlanPayrollType compPlanPayrollType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompPlanPayrollType partially : {}, {}", id, compPlanPayrollType);
        if (compPlanPayrollType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compPlanPayrollType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compPlanPayrollTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompPlanPayrollType> result = compPlanPayrollTypeService.partialUpdate(compPlanPayrollType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compPlanPayrollType.getId().toString())
        );
    }

    /**
     * {@code GET  /comp-plan-payroll-types} : get all the compPlanPayrollTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compPlanPayrollTypes in body.
     */
    @GetMapping("/comp-plan-payroll-types")
    public ResponseEntity<List<CompPlanPayrollType>> getAllCompPlanPayrollTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CompPlanPayrollTypes");
        Page<CompPlanPayrollType> page = compPlanPayrollTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comp-plan-payroll-types/:id} : get the "id" compPlanPayrollType.
     *
     * @param id the id of the compPlanPayrollType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compPlanPayrollType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comp-plan-payroll-types/{id}")
    public ResponseEntity<CompPlanPayrollType> getCompPlanPayrollType(@PathVariable Long id) {
        log.debug("REST request to get CompPlanPayrollType : {}", id);
        Optional<CompPlanPayrollType> compPlanPayrollType = compPlanPayrollTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compPlanPayrollType);
    }

    /**
     * {@code DELETE  /comp-plan-payroll-types/:id} : delete the "id" compPlanPayrollType.
     *
     * @param id the id of the compPlanPayrollType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comp-plan-payroll-types/{id}")
    public ResponseEntity<Void> deleteCompPlanPayrollType(@PathVariable Long id) {
        log.debug("REST request to delete CompPlanPayrollType : {}", id);
        compPlanPayrollTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
