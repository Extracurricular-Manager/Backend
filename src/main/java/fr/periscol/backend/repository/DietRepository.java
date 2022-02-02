package fr.periscol.backend.repository;

import fr.periscol.backend.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Diet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {}
