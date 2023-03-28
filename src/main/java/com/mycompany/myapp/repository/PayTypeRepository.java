package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PayType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PayType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayTypeRepository extends JpaRepository<PayType, Long> {}
