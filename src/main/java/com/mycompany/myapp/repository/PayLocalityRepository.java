package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PayLocality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PayLocality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayLocalityRepository extends JpaRepository<PayLocality, Long> {}
