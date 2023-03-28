package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FacilityOrganizationalUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacilityOrganizationalUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityOrganizationalUnitRepository extends JpaRepository<FacilityOrganizationalUnit, Long> {}
