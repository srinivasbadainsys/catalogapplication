package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PayLocality;
import com.mycompany.myapp.repository.PayLocalityRepository;
import com.mycompany.myapp.service.PayLocalityService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PayLocality}.
 */
@RestController
@RequestMapping("/api")
public class PayLocalityResource {

    private final Logger log = LoggerFactory.getLogger(PayLocalityResource.class);

    private static final String ENTITY_NAME = "payLocality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayLocalityService payLocalityService;

    private final PayLocalityRepository payLocalityRepository;

    public PayLocalityResource(PayLocalityService payLocalityService, PayLocalityRepository payLocalityRepository) {
        this.payLocalityService = payLocalityService;
        this.payLocalityRepository = payLocalityRepository;
    }

    /**
     * {@code POST  /pay-localities} : Create a new payLocality.
     *
     * @param payLocality the payLocality to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payLocality, or with status {@code 400 (Bad Request)} if the payLocality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pay-localities")
    public ResponseEntity<PayLocality> createPayLocality(@RequestBody PayLocality payLocality) throws URISyntaxException {
        log.debug("REST request to save PayLocality : {}", payLocality);
        if (payLocality.getId() != null) {
            throw new BadRequestAlertException("A new payLocality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayLocality result = payLocalityService.save(payLocality);
        return ResponseEntity
            .created(new URI("/api/pay-localities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pay-localities/:id} : Updates an existing payLocality.
     *
     * @param id the id of the payLocality to save.
     * @param payLocality the payLocality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payLocality,
     * or with status {@code 400 (Bad Request)} if the payLocality is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payLocality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pay-localities/{id}")
    public ResponseEntity<PayLocality> updatePayLocality(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PayLocality payLocality
    ) throws URISyntaxException {
        log.debug("REST request to update PayLocality : {}, {}", id, payLocality);
        if (payLocality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payLocality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payLocalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayLocality result = payLocalityService.update(payLocality);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payLocality.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pay-localities/:id} : Partial updates given fields of an existing payLocality, field will ignore if it is null
     *
     * @param id the id of the payLocality to save.
     * @param payLocality the payLocality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payLocality,
     * or with status {@code 400 (Bad Request)} if the payLocality is not valid,
     * or with status {@code 404 (Not Found)} if the payLocality is not found,
     * or with status {@code 500 (Internal Server Error)} if the payLocality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pay-localities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayLocality> partialUpdatePayLocality(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PayLocality payLocality
    ) throws URISyntaxException {
        log.debug("REST request to partial update PayLocality partially : {}, {}", id, payLocality);
        if (payLocality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payLocality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payLocalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayLocality> result = payLocalityService.partialUpdate(payLocality);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payLocality.getId().toString())
        );
    }

    /**
     * {@code GET  /pay-localities} : get all the payLocalities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payLocalities in body.
     */
    @GetMapping("/pay-localities")
    public ResponseEntity<List<PayLocality>> getAllPayLocalities(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PayLocalities");
        Page<PayLocality> page = payLocalityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-localities/:id} : get the "id" payLocality.
     *
     * @param id the id of the payLocality to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payLocality, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pay-localities/{id}")
    public ResponseEntity<PayLocality> getPayLocality(@PathVariable Long id) {
        log.debug("REST request to get PayLocality : {}", id);
        Optional<PayLocality> payLocality = payLocalityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payLocality);
    }

    /**
     * {@code DELETE  /pay-localities/:id} : delete the "id" payLocality.
     *
     * @param id the id of the payLocality to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pay-localities/{id}")
    public ResponseEntity<Void> deletePayLocality(@PathVariable Long id) {
        log.debug("REST request to delete PayLocality : {}", id);
        payLocalityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
