package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Union;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Union entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnionRepository extends JpaRepository<Union, Long> {}
