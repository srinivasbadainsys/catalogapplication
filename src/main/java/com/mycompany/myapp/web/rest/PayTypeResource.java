package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PayType;
import com.mycompany.myapp.repository.PayTypeRepository;
import com.mycompany.myapp.service.PayTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PayType}.
 */
@RestController
@RequestMapping("/api")
public class PayTypeResource {

    private final Logger log = LoggerFactory.getLogger(PayTypeResource.class);

    private static final String ENTITY_NAME = "payType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayTypeService payTypeService;

    private final PayTypeRepository payTypeRepository;

    public PayTypeResource(PayTypeService payTypeService, PayTypeRepository payTypeRepository) {
        this.payTypeService = payTypeService;
        this.payTypeRepository = payTypeRepository;
    }

    /**
     * {@code POST  /pay-types} : Create a new payType.
     *
     * @param payType the payType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payType, or with status {@code 400 (Bad Request)} if the payType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pay-types")
    public ResponseEntity<PayType> createPayType(@RequestBody PayType payType) throws URISyntaxException {
        log.debug("REST request to save PayType : {}", payType);
        if (payType.getId() != null) {
            throw new BadRequestAlertException("A new payType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayType result = payTypeService.save(payType);
        return ResponseEntity
            .created(new URI("/api/pay-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pay-types/:id} : Updates an existing payType.
     *
     * @param id the id of the payType to save.
     * @param payType the payType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payType,
     * or with status {@code 400 (Bad Request)} if the payType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pay-types/{id}")
    public ResponseEntity<PayType> updatePayType(@PathVariable(value = "id", required = false) final Long id, @RequestBody PayType payType)
        throws URISyntaxException {
        log.debug("REST request to update PayType : {}, {}", id, payType);
        if (payType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayType result = payTypeService.update(payType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pay-types/:id} : Partial updates given fields of an existing payType, field will ignore if it is null
     *
     * @param id the id of the payType to save.
     * @param payType the payType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payType,
     * or with status {@code 400 (Bad Request)} if the payType is not valid,
     * or with status {@code 404 (Not Found)} if the payType is not found,
     * or with status {@code 500 (Internal Server Error)} if the payType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pay-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayType> partialUpdatePayType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PayType payType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PayType partially : {}, {}", id, payType);
        if (payType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayType> result = payTypeService.partialUpdate(payType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payType.getId().toString())
        );
    }

    /**
     * {@code GET  /pay-types} : get all the payTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payTypes in body.
     */
    @GetMapping("/pay-types")
    public ResponseEntity<List<PayType>> getAllPayTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PayTypes");
        Page<PayType> page = payTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-types/:id} : get the "id" payType.
     *
     * @param id the id of the payType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pay-types/{id}")
    public ResponseEntity<PayType> getPayType(@PathVariable Long id) {
        log.debug("REST request to get PayType : {}", id);
        Optional<PayType> payType = payTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payType);
    }

    /**
     * {@code DELETE  /pay-types/:id} : delete the "id" payType.
     *
     * @param id the id of the payType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pay-types/{id}")
    public ResponseEntity<Void> deletePayType(@PathVariable Long id) {
        log.debug("REST request to delete PayType : {}", id);
        payTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
