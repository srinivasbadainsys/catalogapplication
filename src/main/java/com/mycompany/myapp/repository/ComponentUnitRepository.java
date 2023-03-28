package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ComponentUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ComponentUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComponentUnitRepository extends JpaRepository<ComponentUnit, Long> {}
