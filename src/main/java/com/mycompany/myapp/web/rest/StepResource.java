package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Step;
import com.mycompany.myapp.repository.StepRepository;
import com.mycompany.myapp.service.StepService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Step}.
 */
@RestController
@RequestMapping("/api")
public class StepResource {

    private final Logger log = LoggerFactory.getLogger(StepResource.class);

    private static final String ENTITY_NAME = "step";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StepService stepService;

    private final StepRepository stepRepository;

    public StepResource(StepService stepService, StepRepository stepRepository) {
        this.stepService = stepService;
        this.stepRepository = stepRepository;
    }

    /**
     * {@code POST  /steps} : Create a new step.
     *
     * @param step the step to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new step, or with status {@code 400 (Bad Request)} if the step has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/steps")
    public ResponseEntity<Step> createStep(@RequestBody Step step) throws URISyntaxException {
        log.debug("REST request to save Step : {}", step);
        if (step.getId() != null) {
            throw new BadRequestAlertException("A new step cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Step result = stepService.save(step);
        return ResponseEntity
            .created(new URI("/api/steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /steps/:id} : Updates an existing step.
     *
     * @param id the id of the step to save.
     * @param step the step to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated step,
     * or with status {@code 400 (Bad Request)} if the step is not valid,
     * or with status {@code 500 (Internal Server Error)} if the step couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/steps/{id}")
    public ResponseEntity<Step> updateStep(@PathVariable(value = "id", required = false) final Long id, @RequestBody Step step)
        throws URISyntaxException {
        log.debug("REST request to update Step : {}, {}", id, step);
        if (step.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, step.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Step result = stepService.update(step);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, step.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /steps/:id} : Partial updates given fields of an existing step, field will ignore if it is null
     *
     * @param id the id of the step to save.
     * @param step the step to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated step,
     * or with status {@code 400 (Bad Request)} if the step is not valid,
     * or with status {@code 404 (Not Found)} if the step is not found,
     * or with status {@code 500 (Internal Server Error)} if the step couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/steps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Step> partialUpdateStep(@PathVariable(value = "id", required = false) final Long id, @RequestBody Step step)
        throws URISyntaxException {
        log.debug("REST request to partial update Step partially : {}, {}", id, step);
        if (step.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, step.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Step> result = stepService.partialUpdate(step);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, step.getId().toString())
        );
    }

    /**
     * {@code GET  /steps} : get all the steps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of steps in body.
     */
    @GetMapping("/steps")
    public ResponseEntity<List<Step>> getAllSteps(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Steps");
        Page<Step> page = stepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /steps/:id} : get the "id" step.
     *
     * @param id the id of the step to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the step, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/steps/{id}")
    public ResponseEntity<Step> getStep(@PathVariable Long id) {
        log.debug("REST request to get Step : {}", id);
        Optional<Step> step = stepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(step);
    }

    /**
     * {@code DELETE  /steps/:id} : delete the "id" step.
     *
     * @param id the id of the step to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/steps/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long id) {
        log.debug("REST request to delete Step : {}", id);
        stepService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
