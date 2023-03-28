package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmploymentStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmploymentStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentStatusRepository extends JpaRepository<EmploymentStatus, Long> {}
