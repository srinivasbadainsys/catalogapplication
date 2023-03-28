package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LocationShift;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocationShift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationShiftRepository extends JpaRepository<LocationShift, Long> {}
