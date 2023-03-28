package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ComponentUnit;
import com.mycompany.myapp.repository.ComponentUnitRepository;
import com.mycompany.myapp.service.ComponentUnitService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ComponentUnit}.
 */
@RestController
@RequestMapping("/api")
public class ComponentUnitResource {

    private final Logger log = LoggerFactory.getLogger(ComponentUnitResource.class);

    private static final String ENTITY_NAME = "componentUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComponentUnitService componentUnitService;

    private final ComponentUnitRepository componentUnitRepository;

    public ComponentUnitResource(ComponentUnitService componentUnitService, ComponentUnitRepository componentUnitRepository) {
        this.componentUnitService = componentUnitService;
        this.componentUnitRepository = componentUnitRepository;
    }

    /**
     * {@code POST  /component-units} : Create a new componentUnit.
     *
     * @param componentUnit the componentUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new componentUnit, or with status {@code 400 (Bad Request)} if the componentUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/component-units")
    public ResponseEntity<ComponentUnit> createComponentUnit(@RequestBody ComponentUnit componentUnit) throws URISyntaxException {
        log.debug("REST request to save ComponentUnit : {}", componentUnit);
        if (componentUnit.getId() != null) {
            throw new BadRequestAlertException("A new componentUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComponentUnit result = componentUnitService.save(componentUnit);
        return ResponseEntity
            .created(new URI("/api/component-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /component-units/:id} : Updates an existing componentUnit.
     *
     * @param id the id of the componentUnit to save.
     * @param componentUnit the componentUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentUnit,
     * or with status {@code 400 (Bad Request)} if the componentUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the componentUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/component-units/{id}")
    public ResponseEntity<ComponentUnit> updateComponentUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComponentUnit componentUnit
    ) throws URISyntaxException {
        log.debug("REST request to update ComponentUnit : {}, {}", id, componentUnit);
        if (componentUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComponentUnit result = componentUnitService.update(componentUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, componentUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /component-units/:id} : Partial updates given fields of an existing componentUnit, field will ignore if it is null
     *
     * @param id the id of the componentUnit to save.
     * @param componentUnit the componentUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated componentUnit,
     * or with status {@code 400 (Bad Request)} if the componentUnit is not valid,
     * or with status {@code 404 (Not Found)} if the componentUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the componentUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/component-units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComponentUnit> partialUpdateComponentUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComponentUnit componentUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComponentUnit partially : {}, {}", id, componentUnit);
        if (componentUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, componentUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!componentUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComponentUnit> result = componentUnitService.partialUpdate(componentUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, componentUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /component-units} : get all the componentUnits.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of componentUnits in body.
     */
    @GetMapping("/component-units")
    public ResponseEntity<List<ComponentUnit>> getAllComponentUnits(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ComponentUnits");
        Page<ComponentUnit> page = componentUnitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /component-units/:id} : get the "id" componentUnit.
     *
     * @param id the id of the componentUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the componentUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/component-units/{id}")
    public ResponseEntity<ComponentUnit> getComponentUnit(@PathVariable Long id) {
        log.debug("REST request to get ComponentUnit : {}", id);
        Optional<ComponentUnit> componentUnit = componentUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(componentUnit);
    }

    /**
     * {@code DELETE  /component-units/:id} : delete the "id" componentUnit.
     *
     * @param id the id of the componentUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/component-units/{id}")
    public ResponseEntity<Void> deleteComponentUnit(@PathVariable Long id) {
        log.debug("REST request to delete ComponentUnit : {}", id);
        componentUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
