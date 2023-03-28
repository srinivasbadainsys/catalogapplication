package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
import com.mycompany.myapp.service.UnionService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Union}.
 */
@RestController
@RequestMapping("/api")
public class UnionResource {

    private final Logger log = LoggerFactory.getLogger(UnionResource.class);

    private static final String ENTITY_NAME = "union";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnionService unionService;

    private final UnionRepository unionRepository;

    public UnionResource(UnionService unionService, UnionRepository unionRepository) {
        this.unionService = unionService;
        this.unionRepository = unionRepository;
    }

    /**
     * {@code POST  /unions} : Create a new union.
     *
     * @param union the union to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new union, or with status {@code 400 (Bad Request)} if the union has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unions")
    public ResponseEntity<Union> createUnion(@RequestBody Union union) throws URISyntaxException {
        log.debug("REST request to save Union : {}", union);
        if (union.getId() != null) {
            throw new BadRequestAlertException("A new union cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Union result = unionService.save(union);
        return ResponseEntity
            .created(new URI("/api/unions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unions/:id} : Updates an existing union.
     *
     * @param id the id of the union to save.
     * @param union the union to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated union,
     * or with status {@code 400 (Bad Request)} if the union is not valid,
     * or with status {@code 500 (Internal Server Error)} if the union couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unions/{id}")
    public ResponseEntity<Union> updateUnion(@PathVariable(value = "id", required = false) final Long id, @RequestBody Union union)
        throws URISyntaxException {
        log.debug("REST request to update Union : {}, {}", id, union);
        if (union.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, union.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Union result = unionService.update(union);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, union.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unions/:id} : Partial updates given fields of an existing union, field will ignore if it is null
     *
     * @param id the id of the union to save.
     * @param union the union to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated union,
     * or with status {@code 400 (Bad Request)} if the union is not valid,
     * or with status {@code 404 (Not Found)} if the union is not found,
     * or with status {@code 500 (Internal Server Error)} if the union couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Union> partialUpdateUnion(@PathVariable(value = "id", required = false) final Long id, @RequestBody Union union)
        throws URISyntaxException {
        log.debug("REST request to partial update Union partially : {}, {}", id, union);
        if (union.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, union.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Union> result = unionService.partialUpdate(union);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, union.getId().toString())
        );
    }

    /**
     * {@code GET  /unions} : get all the unions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unions in body.
     */
    @GetMapping("/unions")
    public ResponseEntity<List<Union>> getAllUnions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Unions");
        Page<Union> page = unionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unions/:id} : get the "id" union.
     *
     * @param id the id of the union to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the union, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unions/{id}")
    public ResponseEntity<Union> getUnion(@PathVariable Long id) {
        log.debug("REST request to get Union : {}", id);
        Optional<Union> union = unionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(union);
    }

    /**
     * {@code DELETE  /unions/:id} : delete the "id" union.
     *
     * @param id the id of the union to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unions/{id}")
    public ResponseEntity<Void> deleteUnion(@PathVariable Long id) {
        log.debug("REST request to delete Union : {}", id);
        unionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
